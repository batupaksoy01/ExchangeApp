package batu.springframework.exchangeapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import batu.springframework.exchangeapp.dto.ConversionDTO;
import batu.springframework.exchangeapp.dto.ConversionInputDTO;
import batu.springframework.exchangeapp.exception.ApiException;
import batu.springframework.exchangeapp.models.Conversion;
import batu.springframework.exchangeapp.repositories.ConversionRepository;

public class GetConversionTest {
	
	private float amount = (float)5;
	private float rate = (float)1.5;
	private float errorCode = (float)-1;
	
	@Mock
	ConversionRepository conversionRepository;
	@InjectMocks
	GetConversionService getConversionService;
	
	public ServiceHelper conversionService() {
        return Mockito.mock(ServiceHelper.class);
    }

	@Test
	public void whenValidInputReceived_thenConversionListReturned() {
		ServiceHelper serviceMock = conversionService();
		Mockito.when(serviceMock.calculateRate("EUR", "USD")).thenReturn(rate);
		GetConversionService testObject = new GetConversionService(serviceMock);
		ConversionInputDTO testInput = new ConversionInputDTO(amount,"EUR", "USD");
		List<ConversionDTO> returnList = testObject.getConversion(testInput).getConversionList();
		assertNotNull(returnList);
		assertEquals(returnList.size(),1);
		ConversionDTO conversion = returnList.get(0);
		assertEquals(conversion.getSource(), "EUR");
		assertEquals(conversion.getTarget(), "USD");
		assertEquals(conversion.getSourceAmount(), amount);
		assertEquals(conversion.getTargetAmount(), rate * amount);
		assertNotNull(conversion.getDateTime());
	}
	
	@Test
	public void whenValidInputReceived_thenConversionSavedToDatabase() {
		ServiceHelper serviceMock = conversionService();
		Mockito.when(serviceMock.calculateRate("EUR", "USD")).thenReturn(rate);
		GetConversionService testObject = new GetConversionService(serviceMock);
		ConversionInputDTO testInput = new ConversionInputDTO(amount,"EUR", "USD");
		ArgumentCaptor<Conversion> captor = ArgumentCaptor.forClass(Conversion.class);
		testObject.getConversion(testInput);
		Mockito.verify(serviceMock, Mockito.times(1)).saveConversion(captor.capture());
		Conversion argument = captor.getValue();
		assertEquals(argument.getSource(), "EUR");
		assertEquals(argument.getTarget(), "USD");
		assertEquals(argument.getSourceAmount(), amount);
		assertEquals(argument.getTargetAmount(), rate * amount);
		assertNotNull(argument.getDateTime());
	}
	
	@Test
	public void whenErrorCodeReceived_thenApiExceptionThrown() {
		ServiceHelper serviceMock = conversionService();
		Mockito.when(serviceMock.calculateRate(Mockito.anyString(), Mockito.anyString())).thenReturn(errorCode);
		Mockito.when(serviceMock.mapError(errorCode)).thenReturn("test");
		ConversionInputDTO testInput = new ConversionInputDTO(amount,"EUR", "USD");
		GetConversionService testObject = new GetConversionService(serviceMock);
		assertThrows(ApiException.class, () -> {
			testObject.getConversion(testInput);
		});
	}
}
