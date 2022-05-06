package batu.springframework.exchangeapp.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import batu.springframework.exchangeapp.data.dto.FailResponseDTO;

class MissingRequestParameterTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	void givenSourceParameterMissing_whenExchangeRateCalled_thenErrorMessageReceived() {
		assertThat(restTemplate.getForObject("http://localhost:8080/exchangeRate?&target=USD", ResponseEntity.class)
				.equals(new ResponseEntity<FailResponseDTO>(new FailResponseDTO("Source or target currency is missing."), HttpStatus.NOT_FOUND)));
	}
	
	@Test
	void givenTargetParameterMissing_whenExchangeRateCalled_thenErrorMessageReceived() {
		assertThat(restTemplate.getForObject("http://localhost:8080/exchangeRate?&source=USD", String.class)
				.equals("404 : \"{\"errorMessage\":\"Source or target currency is missing.\",\"success\":false}\""));
	}

}
