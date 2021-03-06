package batu.springframework.exchangeapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import batu.springframework.exchangeapp.model.dto.ErrorDto;
import batu.springframework.exchangeapp.model.dto.ExchangeRateDto;
import batu.springframework.exchangeapp.testUtil.ErrorDtoChecker;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExchangeRateEndToEndTest {

	@LocalServerPort
	private int port;
	private TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	public void getExchangeRate_RequestParamsValid_ExchangeRateDtoReturned() {
		String path = pathBuilder("EUR", "EUR");
		
		ExchangeRateDto response = restTemplate.getForObject(path, ExchangeRateDto.class);
		
		assertNotNull(response);
		assertEquals("EUR",response.getSource());
		assertEquals("EUR",response.getTarget());
		assertEquals(1,response.getRate());
	}
	
	@Test
	public void getExchangeRate_SourceInvalid_ErrorDtoReturned() {
		String path = pathBuilder("aba", "USD");
		
		ErrorDto response = restTemplate.getForObject(path, ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(response, 400, "invalid_currency", 
				"Source currency symbol isn't supported or multiple source currencies are provided.");
	}
	
	@Test
	public void getExchangeRate_TargetInvalid_ErrorDtoReturned() {
		String path = pathBuilder("EUR", "aba");
		
		ErrorDto response = restTemplate.getForObject(path, ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(response, 400, "invalid_currency", 
				"Target currency symbol isn't supported or multiple target currencies are provided.");
	}
	
	private String pathBuilder(String source, String target) {
		 return String.format("http://localhost:%d/api/exchange-rate?source=%s&target=%s", port, source, target);
	}
}
