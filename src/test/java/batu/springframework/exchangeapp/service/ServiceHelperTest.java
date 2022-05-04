package batu.springframework.exchangeapp.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import batu.springframework.exchangeapp.FixerResponse;

public class ServiceHelperTest {
	
	ServiceHelper testObject = new ServiceHelper();
	
	//EUR is different from other currencies because the it is the default base for rate calculation and is not 
	//changeable by free endpoints 
	
	//EUR,X
	@Test
	public void whenSourceIsEUR_thenResponseIsParseable() {
		FixerResponse testResponse = testObject.makeApiCall("EUR", "USD");
		assertTrue(testResponse.isSuccess());
		Map<String,Float> rates = testResponse.getRates();
		assertNotNull(rates);
		assertEquals(rates.size(),2);
		assertTrue(rates.containsKey("EUR") && rates.get("EUR") != null);
		assertTrue(rates.containsKey("USD") && rates.get("USD") != null);
	}

	//X,EUR
	@Test
	public void whenTargetIsEUR_thenResponseIsParseable() {
		FixerResponse testResponse = testObject.makeApiCall("USD", "EUR");
		assertTrue(testResponse.isSuccess());
		Map<String,Float> rates = testResponse.getRates();
		assertNotNull(rates);
		assertEquals(rates.size(),2);
		assertTrue(rates.containsKey("EUR") && rates.get("EUR") != null);
		assertTrue(rates.containsKey("USD") && rates.get("USD") != null);
	}
	
	//X,X
	@Test
	public void whenNeitherIsEUR_thenResponseIsParseable() {
		FixerResponse testResponse = testObject.makeApiCall("GBP", "USD");
		assertTrue(testResponse.isSuccess());
		Map<String,Float> rates = testResponse.getRates();
		assertNotNull(rates);
		assertEquals(rates.size(),2);
		assertTrue(rates.containsKey("GBP") && rates.get("GBP") != null);
		assertTrue(rates.containsKey("USD") && rates.get("USD") != null);
	}
	
	@Test
	public void whenApiReturnsError_thenResponseIsParsable() {
		FixerResponse testResponse = testObject.makeApiCall("x", "y");
		assertFalse(testResponse.isSuccess());
		Map<String,String> error = testResponse.getError();
		assertNotNull(error);
		assertTrue(error.containsKey("code") && error.get("code") != null);
	}
	
	@Test
	public void whenResponseIsPositive_theRateCalculated() {
		ServiceHelper serviceMock = Mockito.mock(ServiceHelper.class);
		FixerResponse mockResponse = new FixerResponse();
		mockResponse.setSuccess(true);
		mockResponse.setRates(Map.of(
			    "EUR", (float)1.5,
			    "USD", (float)7.5
			));
		Mockito.when(serviceMock.makeApiCall("EUR", "USD")).thenReturn(mockResponse);
		Mockito.when(serviceMock.calculateRate(Mockito.anyString(), Mockito.anyString())).thenCallRealMethod();
		assertEquals(serviceMock.calculateRate("EUR", "USD"), (float)5, 0.00001);
	}
	
	@Test
	public void whenResponseIsNegative_thenErrorCodeReturned() {
		ServiceHelper serviceMock = Mockito.mock(ServiceHelper.class);
		FixerResponse mockResponse = new FixerResponse();
		mockResponse.setSuccess(false);
		mockResponse.setError(Map.of(
			    "code", "102"
			));
		Mockito.when(serviceMock.makeApiCall("EUR", "USD")).thenReturn(mockResponse);
		Mockito.when(serviceMock.calculateRate(Mockito.anyString(), Mockito.anyString())).thenCallRealMethod();
		assertEquals(serviceMock.calculateRate("EUR", "USD"), (float)-102, 0.0000001);
	}
	
	@Test
	public void whenErrorCodeBiggerThan100_IsMappedCorrectly() {
		String message = testObject.mapError((float)-101);
		assertEquals(message, "Requested endpoint can't be reached, please try another endpoint or different parameters.");
	}
	
	@Test
	public void whenErrorCode202_IsMappedCorrectly() {
		String message = testObject.mapError((float)-202);
		assertEquals(message, "One or more currency symbols are not supported.");
	}
	
	@Test
	public void whenErrorCode404_IsMappedCorrectly() {
		String message = testObject.mapError((float)-404);
		assertEquals(message, "The requested resource does not exist.");
	}
}
