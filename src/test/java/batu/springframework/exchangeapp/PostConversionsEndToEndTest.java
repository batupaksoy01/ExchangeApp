package batu.springframework.exchangeapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import batu.springframework.exchangeapp.dao.entity.ConversionEntity;
import batu.springframework.exchangeapp.dao.repository.ConversionRepository;
import batu.springframework.exchangeapp.model.dto.ConversionDto;
import batu.springframework.exchangeapp.model.dto.ConversionInputDto;
import batu.springframework.exchangeapp.model.dto.ErrorDto;
import batu.springframework.exchangeapp.testUtil.ConversionComparator;
import batu.springframework.exchangeapp.testUtil.ErrorDtoChecker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostConversionsEndToEndTest implements ConversionComparator{
	@LocalServerPort
	private int port;
	private TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Autowired
	private ConversionRepository conversionRepository;
	
	private ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	
	@Test
	public void postConversion_RequestBodyValid_SavedToDbConversionDtoReturned() {
		ConversionInputDto testInput = new ConversionInputDto(100.0, "EUR", "USD");
		
		ConversionDto response = restTemplate.postForObject(getConversionResourcePath(port), 
				postConversionRequestBuilder(testInput) , ConversionDto.class);
		
		compareConversionDtoToInputDto(response, testInput);
		
		List<ConversionEntity> repoContent = conversionRepository.findAll();
		
		assertEquals(1, repoContent.size());
		compareConversionDtoToEntity(response, repoContent.get(0));
	}
	
	@Test
	public void postConversion_SourceInvalid_ErrorDtoReturned() {
		ErrorDto response = restTemplate.postForObject(getConversionResourcePath(port), 
				postConversionRequestBuilder(new ConversionInputDto(100.0, "asda", "USD")) , ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(response, 400, "invalid_currency", 
				"Source currency symbol isn't supported or multiple source currencies are provided.");
	}
	
	@Test
	public void postConversion_TargetInvalid_ErrorDtoReturned() {
		ErrorDto response = restTemplate.postForObject(getConversionResourcePath(port), 
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
}
