package batu.springframework.exchangeapp.service;

import batu.springframework.exchangeapp.dto.ConversionDTO;
import batu.springframework.exchangeapp.exception.ApiException;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateTest {
	
	private Float rate = (float)1.5;
	private Float errorCode = (float)-1;
	
	public ServiceHelper conversionService() {
        return Mockito.mock(ServiceHelper.class);
    }
	
	@Test
	public void whenRateReceived_thenConversionListReturned() {
		ServiceHelper serviceMock = conversionService();
		Mockito.when(serviceMock.calculateRate("EUR", "USD")).thenReturn(rate);
		ExchangeRateService testObject = new ExchangeRateService(serviceMock);
		List<ConversionDTO> returnList = testObject.getExchangeRate("EUR", "USD").getConversionList();
		assertNotNull(returnList);
		assertEquals(returnList.size(),1);
		ConversionDTO conversion = returnList.get(0);
		assertEquals(conversion.getSource(), "EUR");
		assertEquals(conversion.getTarget(), "USD");
		assertEquals(conversion.getSourceAmount(), 1);
		assertEquals(conversion.getTargetAmount(), rate);
		assertNotNull(conversion.getDateTime());
	}
	
	@Test
	public void whenErrorCodeReceived_thenApiExceptionThrown() {
		ServiceHelper serviceMock = conversionService();
		Mockito.when(serviceMock.calculateRate(Mockito.anyString(), Mockito.anyString())).thenReturn(errorCode);
		Mockito.when(serviceMock.mapError(errorCode)).thenReturn("test");
		ExchangeRateService testObject = new ExchangeRateService(serviceMock);
		assertThrows(ApiException.class, () -> {
			testObject.getExchangeRate("","");
		});
	}
	
}
