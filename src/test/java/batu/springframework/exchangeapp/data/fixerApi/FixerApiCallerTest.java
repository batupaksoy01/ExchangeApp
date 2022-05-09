package batu.springframework.exchangeapp.data.fixerApi;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.junit.jupiter.api.Assertions;

import batu.springframework.exchangeapp.data.dto.FixerResponseDTO;
import batu.springframework.exchangeapp.data.exception.ApiException;

public class FixerApiCallerTest {

	FixerApiCaller testObject = new FixerApiCaller();
	
	//EUR is different from other currencies because the it is the default base for rate calculation and is not 
	//changeable by free endpoints 
	
	//EUR,X
	@Test
	public void whenSourceIsEUR_thenResponseIsParseable() {
		FixerResponseDTO testResponse = testObject.makeApiCall("EUR", "USD");
		Assertions.assertTrue(testResponse.isSuccess());
		Map<String,Float> rates = testResponse.getRates();
		Assertions.assertNotNull(rates);
		Assertions.assertEquals(rates.size(),2);
		Assertions.assertTrue(rates.containsKey("EUR") && rates.get("EUR") != null);
		Assertions.assertTrue(rates.containsKey("USD") && rates.get("USD") != null);
	}

	//X,EUR
	@Test
	public void whenTargetIsEUR_thenResponseIsParseable() {
		FixerResponseDTO testResponse = testObject.makeApiCall("USD", "EUR");
		Assertions.assertTrue(testResponse.isSuccess());
		Map<String,Float> rates = testResponse.getRates();
		Assertions.assertNotNull(rates);
		Assertions.assertEquals(rates.size(),2);
		Assertions.assertTrue(rates.containsKey("EUR") && rates.get("EUR") != null);
		Assertions.assertTrue(rates.containsKey("USD") && rates.get("USD") != null);
	}
	
	//X,X
	@Test
	public void whenNeitherIsEUR_thenResponseIsParseable() {
		FixerResponseDTO testResponse = testObject.makeApiCall("GBP", "USD");
		Assertions.assertTrue(testResponse.isSuccess());
		Map<String,Float> rates = testResponse.getRates();
		Assertions.assertNotNull(rates);
		Assertions.assertEquals(rates.size(),2);
		Assertions.assertTrue(rates.containsKey("GBP") && rates.get("GBP") != null);
		Assertions.assertTrue(rates.containsKey("USD") && rates.get("USD") != null);
	}
	
	@Test
	public void whenApiReturns_thenResponseIsChecked() {
		FixerApiCaller callerMock = Mockito.mock(FixerApiCaller.class);
		Mockito.when(callerMock.makeApiCall(Mockito.anyString(), Mockito.anyString())).thenCallRealMethod();
		callerMock.makeApiCall("GBP", "USD");
		Mockito.verify(callerMock, Mockito.times(1)).checkFixerResponse(Mockito.any(FixerResponseDTO.class));
	}
	
	@Test
	public void whenErrorCodeIsNull_thenExceptionThrown() {
		FixerResponseDTO testInput = new FixerResponseDTO();
		testInput.setSuccess(false);
		ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {testObject.checkFixerResponse(testInput);});
		Assertions.assertEquals(e.getStatus(), HttpStatus.BAD_GATEWAY);
	}
	
	@Test
	public void whenErrorCodeIsUnique_thenDefaultExceptionThrown() {
		FixerResponseDTO testInput = new FixerResponseDTO();
		testInput.setSuccess(false);
		testInput.setError(Map.of("code", "205"));
		ResponseStatusException e = Assertions.assertThrows(ResponseStatusException.class, () -> {testObject.checkFixerResponse(testInput);});
		Assertions.assertEquals(e.getStatus(), HttpStatus.BAD_GATEWAY);
	}
	
	@Test
	public void whenErrorCode202_thenExceptionThrown() {
		FixerResponseDTO testInput = new FixerResponseDTO();
		testInput.setSuccess(false);
		testInput.setError(Map.of("code", "202"));
		ApiException e = Assertions.assertThrows(ApiException.class, () -> {testObject.checkFixerResponse(testInput);});
		Assertions.assertEquals(e.getErrorMessage(), "One or more currency symbols are not supported.");
	}
	
	@Test
	public void whenErrorCode404_thenExceptionThrown() {
		FixerResponseDTO testInput = new FixerResponseDTO();
		testInput.setSuccess(false);
		testInput.setError(Map.of("code", "404"));
		ApiException e = Assertions.assertThrows(ApiException.class, () -> {testObject.checkFixerResponse(testInput);});
		Assertions.assertEquals(e.getErrorMessage(), "The requested resource does not exist.");
	}
}
