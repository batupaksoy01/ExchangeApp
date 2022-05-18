package batu.springframework.exchangeapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import batu.springframework.exchangeapp.dao.entity.ConversionEntity;
import batu.springframework.exchangeapp.dao.repository.ConversionRepository;
import batu.springframework.exchangeapp.model.dto.ConversionDto;
import batu.springframework.exchangeapp.testUtil.ConversionComparator;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetConversionsEndToEndTest implements ConversionComparator {
	@LocalServerPort
	private int port;
	private TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Autowired
	private ConversionRepository conversionRepository;
	
	private static List<ConversionEntity> repoContent = List.of(
			new ConversionEntity(15.0, 50.0, "TRY", "CAD"),
			new ConversionEntity(100.0, 110.0, "USD", "EUR"),
			new ConversionEntity(12.0, 125.0, "TRY", "EUR"));
	
	@BeforeAll
	public void fillRepo() {
		for (ConversionEntity inputEntity: repoContent) {
			conversionRepository.save(inputEntity);
		}
	}
	
	@Test
	public void getConversions_PageGiven_ConversionDtoListReturned() {
		List<ConversionDto> response = makeGetConversionsCall(getConversionResourcePath(port));
		
		assertNotNull(response);
		assertEquals(repoContent.size(),response.size());
		for(int i = 0; i < response.size(); i++) {
			compareConversionDtoToEntity(response.get(i), repoContent.get(i));
		}
	}
	
	@Test
	public void getConversions_PageNotGiven_ConversionDtoListReturned() {
		List<ConversionDto> response = makeGetConversionsCall(getConversionResourcePath(port) + "?page=0&size=2");
		
		assertNotNull(response);
		assertEquals(2,response.size());
		compareConversionDtoToEntity(response.get(0), repoContent.get(0));
		compareConversionDtoToEntity(response.get(1), repoContent.get(1));
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
