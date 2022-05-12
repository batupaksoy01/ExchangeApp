package batu.springframework.exchangeapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.model.dtos.FixerResponseDto;
import batu.springframework.exchangeapp.model.exceptions.WrongInputException;
import batu.springframework.exchangeapp.services.ServiceHelper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;


public class ServiceHelperTest {
	
	FixerApiCaller apiCallerMock = Mockito.mock(FixerApiCaller.class);
	ServiceHelper testObject = new ServiceHelper(apiCallerMock);
	FixerResponseDto mockResponse = new FixerResponseDto();
	
	
	@Test
	public void givenSourceAndTarget_thenApiCallerMethodCalled() {
		mockResponse.setSuccess(true);
		mockResponse.setRates(Map.of(
			    "EUR", (float)1.5,
			    "USD", (float)7.5
			));
		Mockito.when(apiCallerMock.makeApiCall(Mockito.anyString(), Mockito.anyString())).thenReturn(mockResponse);
		testObject.calculateRate("EUR", "USD");
		Mockito.verify(apiCallerMock, Mockito.times(1)).makeApiCall("EUR","USD");
	}
	
	@Test
	public void whenResponseIsPositive_theRateCalculated() {
		mockResponse.setSuccess(true);
		mockResponse.setRates(Map.of(
			    "EUR", (float)1.5,
			    "USD", (float)7.5
			));
		Mockito.when(apiCallerMock.makeApiCall(Mockito.anyString(), Mockito.anyString())).thenReturn(mockResponse);
		assertEquals(testObject.calculateRate("EUR", "USD"), (float)5, 0.00001);
	}
	
	@Test
	public void whenTargetIsEmpty_thenExceptionThrown() {
		mockResponse.setSuccess(true);
		mockResponse.setRates(Map.of(
			    "EUR", (float)1.5
			));
		Mockito.when(apiCallerMock.makeApiCall(Mockito.anyString(), Mockito.anyString())).thenReturn(mockResponse);
		WrongInputException e = Assertions.assertThrows(WrongInputException.class, () -> {testObject.calculateRate("EUR","USD");});
		Assertions.assertEquals("Target currency symbol isn't supported or multiple target currencies are provided.", 
				e.getErrorMessage());
	}
	
	@Test
	public void whenTargetNameIsWrong_thenExceptionThrown() {
		mockResponse.setSuccess(true);
		mockResponse.setRates(Map.of(
			    "EUR", (float)1.5,
			    "USR", (float)7.5
			));
		Mockito.when(apiCallerMock.makeApiCall(Mockito.anyString(), Mockito.anyString())).thenReturn(mockResponse);
		WrongInputException e = Assertions.assertThrows(WrongInputException.class, () -> {testObject.calculateRate("EUR","USD");});
		Assertions.assertEquals("Target currency symbol isn't supported or multiple target currencies are provided.", 
				e.getErrorMessage());
	}
	
	@Test
	public void whenSourceIsEmpty_thenExceptionThrown() {
		mockResponse.setSuccess(true);
		mockResponse.setRates(Map.of(
			    "USD", (float)7.5
			));
		Mockito.when(apiCallerMock.makeApiCall(Mockito.anyString(), Mockito.anyString())).thenReturn(mockResponse);
		WrongInputException e = Assertions.assertThrows(WrongInputException.class, () -> {testObject.calculateRate("EUR","USD");});
		Assertions.assertEquals("Source currency symbol isn't supported or multiple source currencies are provided.", 
				e.getErrorMessage());
	}
	
	@Test
	public void whenSourceNameIsWrong_thenExceptionThrown() {
		mockResponse.setSuccess(true);
		mockResponse.setRates(Map.of(
			    "EUD", (float)1.5,
			    "USD", (float)7.5
			));
		Mockito.when(apiCallerMock.makeApiCall(Mockito.anyString(), Mockito.anyString())).thenReturn(mockResponse);
		WrongInputException e = Assertions.assertThrows(WrongInputException.class, () -> {testObject.calculateRate("EUR","USD");});
		Assertions.assertEquals("Source currency symbol isn't supported or multiple source currencies are provided.", 
				e.getErrorMessage());
	}
}
