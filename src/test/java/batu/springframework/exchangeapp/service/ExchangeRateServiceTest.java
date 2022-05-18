package batu.springframework.exchangeapp.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.model.dto.ExchangeRateDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeRateServiceTest {
	
	private FixerApiCaller apiCallerMock = mock(FixerApiCaller.class);
	private ExchangeRateService testObject = new ExchangeRateService(apiCallerMock);
	
	private static final String GENERIC_SOURCE = "EUR";
	private static final String GENERIC_TARGET = "USD";
	
	@Test
	public void getExchangeRate_MethodCalled_ApiCallMade() {
		testObject.getExchangeRate(GENERIC_SOURCE, GENERIC_TARGET);
		
		verify(apiCallerMock, times(1)).getApiResult(GENERIC_SOURCE, GENERIC_TARGET, 1.0);
	}
	
	@Test
	public void getExchangeRate_ApiReturned_ExchangeRateDtoReturned() {
		when(apiCallerMock.getApiResult(anyString(), anyString(), any(Double.class))).thenReturn(5.0);
		
		ExchangeRateDto returnValue = testObject.getExchangeRate(GENERIC_SOURCE, GENERIC_TARGET);
		
		assertEquals(GENERIC_SOURCE, returnValue.getSource());
		assertEquals(GENERIC_TARGET, returnValue.getTarget());
		assertEquals(5.0, returnValue.getRate());
	}
}
