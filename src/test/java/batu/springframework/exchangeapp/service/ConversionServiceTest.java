package batu.springframework.exchangeapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.dao.entity.ConversionEntity;
import batu.springframework.exchangeapp.dao.repository.ConversionRepository;
import batu.springframework.exchangeapp.model.dto.ConversionDto;
import batu.springframework.exchangeapp.model.dto.ConversionInputDto;

public class ConversionServiceTest {
	
	private static ConversionService testObject;
	private FixerApiCaller apiCallerMock = Mockito.mock(FixerApiCaller.class);
	private ConversionRepository repositoryMock = Mockito.mock(ConversionRepository.class);
	
	private static final Double GET_RATE_RETURN_VALUE = 750.0;
	
	private static final Double GENERIC_SOURCE_AMOUNT = 150.0;
	private static final String GENERIC_SOURCE = "EUR";
	private static final String GENERIC_TARGET = "USD";
	
	private ConversionInputDto genericConversionInput = 
			new ConversionInputDto(GENERIC_SOURCE_AMOUNT, GENERIC_SOURCE, GENERIC_TARGET);		
	
	@Test
	public void postConversion_ConversionSavedToDb_ConversionMappedCorrectly() {
		Mockito.when(apiCallerMock.getConversionRate(Mockito.anyString(), Mockito.anyString(), Mockito.any(Double.class)))
			.thenReturn(GET_RATE_RETURN_VALUE);
		
		ArgumentCaptor<ConversionEntity> entityCaptor = ArgumentCaptor.forClass(ConversionEntity.class);
		
		testObject = new ConversionService(repositoryMock, apiCallerMock);
		testObject.postConversion(genericConversionInput);
		
		Mockito.verify(repositoryMock, Mockito.times(1)).save(entityCaptor.capture());
		ConversionEntity savedEntity = entityCaptor.getValue();
		assertEquals(GENERIC_SOURCE, savedEntity.getSource());
		assertEquals(GENERIC_TARGET, savedEntity.getTarget());
		assertEquals(GENERIC_SOURCE_AMOUNT, savedEntity.getSourceAmount());
		assertEquals(GET_RATE_RETURN_VALUE, savedEntity.getTargetAmount());
	}
	
	@Test
	public void postConversion_BeforeMethodReturn_ReturnValueMappedCorrectly() {
		Mockito.when(apiCallerMock.getConversionRate(Mockito.anyString(), Mockito.anyString(), Mockito.any(Double.class)))
			.thenReturn(GET_RATE_RETURN_VALUE);
		
		testObject = new ConversionService(repositoryMock, apiCallerMock);
		ConversionDto returnValue = testObject.postConversion(genericConversionInput);
		
		checkConversionDtoFields(returnValue);
	}
	
	@Test
	public void getConversions_DbReturnedConversionList_ElementsMappedCorrectly() {
		ConversionEntity dbAnswerConversion = new ConversionEntity();
		dbAnswerConversion.setSource(GENERIC_SOURCE);
		dbAnswerConversion.setTarget(GENERIC_TARGET);
		dbAnswerConversion.setSourceAmount(GENERIC_SOURCE_AMOUNT);
		dbAnswerConversion.setTargetAmount(GET_RATE_RETURN_VALUE);
		List<ConversionEntity> dbFindAllResponseContent = List.of(dbAnswerConversion, dbAnswerConversion);
		
		Mockito.when(repositoryMock.findAll(Mockito.any(Pageable.class)))
			.thenReturn(new PageImpl<>(dbFindAllResponseContent));
		
		testObject = new ConversionService(repositoryMock, apiCallerMock);
		List<ConversionDto> returnList = testObject.getConversions(PageRequest.of(1, 1));
		
		assertNotNull(returnList);
		assertEquals(2, returnList.size());
		for (ConversionDto listElement : returnList) {
			checkConversionDtoFields(listElement);
		}
		
	}
	
	private void checkConversionDtoFields(ConversionDto conversionDto) {
		assertNotNull(conversionDto);
		assertEquals(GENERIC_SOURCE, conversionDto.getSource());
		assertEquals(GENERIC_TARGET, conversionDto.getTarget());
		assertEquals(GENERIC_SOURCE_AMOUNT, conversionDto.getSourceAmount());
		assertEquals(GET_RATE_RETURN_VALUE, conversionDto.getTargetAmount());
	}
}
