package batu.springframework.exchangeapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import batu.springframework.exchangeapp.data.dto.ConversionDTO;
import batu.springframework.exchangeapp.data.dto.ConversionInputDTO;
import batu.springframework.exchangeapp.data.model.Conversion;
import batu.springframework.exchangeapp.data.repository.ConversionRepository;

public class GetConversionTest {
	
	private float amount = (float)5;
	private float rate = (float)1.5;
	ConversionInputDTO testInput = new ConversionInputDTO(amount,"EUR","USD");
	
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
		List<ConversionDTO> returnList = testObject.getConversion(testInput).getConversionList();
		assertNotNull(returnList);
		assertEquals(returnList.size(),1);
		ConversionDTO conversion = returnList.get(0);
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
