package batu.springframework.exchangeapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import batu.springframework.exchangeapp.data.dtos.ConversionDto;
import batu.springframework.exchangeapp.data.dtos.ConversionInputDto;
import batu.springframework.exchangeapp.data.models.Conversion;
import batu.springframework.exchangeapp.data.repositories.ConversionRepository;
import batu.springframework.exchangeapp.services.ServiceHelper;

public class GetConversionTest {
	
	private float amount = (float)5;
	private float rate = (float)1.5;
	ConversionInputDto testInput = new ConversionInputDto(amount,"EUR","USD");
	
	ConversionRepository repositoryMock = Mockito.mock(ConversionRepository.class);
	ServiceHelper helperMock = Mockito.mock(ServiceHelper.class);
	GetConversionService testObject = new GetConversionService(repositoryMock, helperMock);
	
	@Test
	public void givenSourceAndTarget_thenServiceMethodCalled() {
		Mockito.when(helperMock.calculateRate("EUR", "USD")).thenReturn(rate);
		testObject.getConversion(testInput);
		Mockito.verify(helperMock, Mockito.times(1)).calculateRate("EUR","USD");
	}

	@Test
	public void whenValidRateReceived_thenConversionListReturned() {
		Mockito.when(helperMock.calculateRate("EUR", "USD")).thenReturn(rate);
		List<ConversionDto> returnList = testObject.getConversion(testInput).getConversionList();
		assertNotNull(returnList);
		assertEquals(returnList.size(),1);
		ConversionDto conversion = returnList.get(0);
		assertEquals(conversion.getSource(), "EUR");
		assertEquals(conversion.getTarget(), "USD");
		assertEquals(conversion.getSourceAmount(), amount);
		assertEquals(conversion.getTargetAmount(), 7.5);
		assertNotNull(conversion.getDateTime());
	}
	
	@Test
	public void whenValidRateReceived_thenConversionSavedToDatabase() {
		Mockito.when(helperMock.calculateRate("EUR", "USD")).thenReturn(rate);
		ArgumentCaptor<Conversion> captor = ArgumentCaptor.forClass(Conversion.class);
		testObject.getConversion(testInput);
		Mockito.verify(repositoryMock, Mockito.times(1)).save(captor.capture());
		Conversion argument = captor.getValue();
		assertEquals(argument.getSource(), "EUR");
		assertEquals(argument.getTarget(), "USD");
		assertEquals(argument.getSourceAmount(), amount);
		assertEquals(argument.getTargetAmount(), rate * amount);
		assertNotNull(argument.getDateTime());
	}
}
