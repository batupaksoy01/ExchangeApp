package batu.springframework.exchangeapp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.model.dto.ExchangeRateDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeRateServiceTest {
	
	private static ExchangeRateService testObject;
	private static FixerApiCaller apiCallerMock;
	private static final Double GET_RATE_RETURN_VALUE = 5.0;
	private static final String GENERIC_SOURCE = "EUR";
	private static final String GENERIC_TARGET = "USD";
	
	@BeforeAll
	private static void mockInjectionSetup() {
		apiCallerMock = Mockito.mock(FixerApiCaller.class);
		Mockito.when(apiCallerMock.getConversionResult(Mockito.anyString(), Mockito.anyString(), Mockito.any(Double.class)))
			.thenReturn(GET_RATE_RETURN_VALUE);
		
		testObject = new ExchangeRateService(apiCallerMock);
	}
	
	@Test
	public void getExchangeRate_MethodCalled_ApiCallMade() {
		testObject.getExchangeRate(GENERIC_SOURCE, GENERIC_TARGET);
		
		Mockito.verify(apiCallerMock, Mockito.times(1)).getConversionResult(GENERIC_SOURCE, GENERIC_TARGET, 1.0);
	}
	
	@Test
	public void getExchangeRate_ApiReturned_ExchangeRateDtoReturned() {
		ExchangeRateDto returnValue = testObject.getExchangeRate(GENERIC_SOURCE, GENERIC_TARGET);
		
		assertEquals(GENERIC_SOURCE, returnValue.getSource());
		assertEquals(GENERIC_TARGET, returnValue.getTarget());
		assertEquals(GET_RATE_RETURN_VALUE, returnValue.getRate());
	}
}
