package batu.springframework.exchangeapp.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import batu.springframework.exchangeapp.data.dtos.ConversionDto;
import batu.springframework.exchangeapp.services.ExchangeRateService;
import batu.springframework.exchangeapp.services.ServiceHelper;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateTest {
	
	private Float rate = (float)1.5;
	
	public ServiceHelper conversionService() {
        return Mockito.mock(ServiceHelper.class);
    }
	
	@Test
	public void givenSourceAndTarget_thenServiceMethodCalled() {
		ServiceHelper serviceMock = conversionService();
		Mockito.when(serviceMock.calculateRate("EUR", "USD")).thenReturn(rate);
		ExchangeRateService testObject = new ExchangeRateService(serviceMock);
		testObject.getExchangeRate("EUR", "USD");
		Mockito.verify(serviceMock, Mockito.times(1)).calculateRate("EUR","USD");
	}
	
	@Test
	public void whenValidRateReceived_thenConversionListReturned() {
		ServiceHelper serviceMock = conversionService();
		Mockito.when(serviceMock.calculateRate("EUR", "USD")).thenReturn(rate);
		ExchangeRateService testObject = new ExchangeRateService(serviceMock);
		List<ConversionDto> returnList = testObject.getExchangeRate("EUR", "USD").getConversionList();
		assertNotNull(returnList);
		assertEquals(returnList.size(),1);
		ConversionDto conversion = returnList.get(0);
		assertEquals(conversion.getSource(), "EUR");
		assertEquals(conversion.getTarget(), "USD");
		assertEquals(conversion.getSourceAmount(), 1);
		assertEquals(conversion.getTargetAmount(), rate);
		assertNotNull(conversion.getDateTime());
	}
	
}
