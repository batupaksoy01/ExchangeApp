package batu.springframework.exchangeapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import batu.springframework.exchangeapp.model.dto.ConversionDto;
import batu.springframework.exchangeapp.model.dto.ConversionInputDto;
import batu.springframework.exchangeapp.model.dto.ErrorDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class ConversionsEndToEndTest {
	@LocalServerPort
	private int port;
	private TestRestTemplate restTemplate = new TestRestTemplate();
	
	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	List<ConversionInputDto> testInputforGetConversions = List.of(
			new ConversionInputDto(15.0,"TRY", "CAD"),
			new ConversionInputDto(100.0, "USD", "EUR"),
			new ConversionInputDto(12.0, "TRY", "EUR"));
	
	@Test
	@Order(1)
	public void getConversions_PageGiven_ConversionDtoListReturned() {
		for (ConversionInputDto inputDto: testInputforGetConversions) {
			restTemplate.postForObject(getConversionResourcePath(), 
					postConversionRequestBuilder(inputDto) , String.class);
		}
		
		List<ConversionDto> response = makeGetConversionsCall(getConversionResourcePath());
		
		assertNotNull(response);
		assertEquals(testInputforGetConversions.size(),response.size());
		for(int i = 0; i < response.size(); i++) {
			compareConversionDtos(response.get(i), testInputforGetConversions.get(i));
		}
	}
	
	@Test
	@Order(2)
	public void getConversions_PageNotGiven_ConversionDtoListReturned() {
		List<ConversionDto> response = makeGetConversionsCall(getConversionResourcePath() + "?page=0&size=2");
		
		assertNotNull(response);
		assertEquals(2,response.size());
		compareConversionDtos(response.get(0), testInputforGetConversions.get(0));
		compareConversionDtos(response.get(1), testInputforGetConversions.get(1));
	}
	
	@Test
	@Order(3)
	public void postConversion_RequestBodyValid_ConversionDtoReturned() {
		ConversionInputDto testInput = new ConversionInputDto(100.0, "EUR", "USD");
		ConversionDto response = restTemplate.postForObject(getConversionResourcePath(), 
				postConversionRequestBuilder(testInput) , ConversionDto.class);
		
		compareConversionDtos(response, testInput);
	}
	
	@Test
	@Order(3)
	public void postConversion_SourceInvalid_ErrorDtoReturned() {
		ErrorDto response = restTemplate.postForObject(getConversionResourcePath(), 
				postConversionRequestBuilder(new ConversionInputDto(100.0, "asda", "USD")) , ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(response, 400, "invalid_currency", 
				"Source currency symbol isn't supported or multiple source currencies are provided.");
	}
	
	@Test
	@Order(3)
	public void postConversion_TargetInvalid_ErrorDtoReturned() {
		ErrorDto response = restTemplate.postForObject(getConversionResourcePath(), 
				postConversionRequestBuilder(new ConversionInputDto(100.0, "EUR", "asda")) , ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(response, 400, "invalid_currency", 
				"Target currency symbol isn't supported or multiple target currencies are provided.");
	}
	
	private HttpEntity<String> postConversionRequestBuilder(ConversionInputDto inputDto) {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = null;
		try {
			request = new HttpEntity<String>(ow.writeValueAsString(inputDto), headers);
		} catch (JsonProcessingException e) {
			fail("ConversionInput couldn't mapped into JSON.");
		}
		return request;
	}
	
	private String getConversionResourcePath() {
		return "http://localhost:" + port + "/api/conversions";
	}
	
	private void compareConversionDtos(ConversionDto returnedDto, ConversionInputDto expectedDto) {
		assertNotNull(returnedDto);
		assertEquals(expectedDto.getSource(),returnedDto.getSource());
		assertEquals(expectedDto.getTarget(),returnedDto.getTarget());
		assertEquals(expectedDto.getSourceAmount(),returnedDto.getSourceAmount());
		assertNotNull(returnedDto.getTargetAmount());
	}
	
	private List<ConversionDto> makeGetConversionsCall(String path) {
		String jsonResponse = restTemplate.getForObject(path, String.class);
		List<ConversionDto> response = null;
		try {
			response = new ObjectMapper().readValue(jsonResponse, new TypeReference<List<ConversionDto>>(){});
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return response;
	}
}
