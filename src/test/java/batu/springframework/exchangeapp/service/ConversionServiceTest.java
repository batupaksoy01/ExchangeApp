package batu.springframework.exchangeapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.dao.entity.ConversionEntity;
import batu.springframework.exchangeapp.dao.repository.ConversionRepository;
import batu.springframework.exchangeapp.mapper.ConversionMapper;
import batu.springframework.exchangeapp.model.dto.ConversionDto;
import batu.springframework.exchangeapp.model.dto.ConversionInputDto;
import batu.springframework.exchangeapp.testUtil.ConversionComparator;

public class ConversionServiceTest  implements ConversionComparator{
	
	private FixerApiCaller apiCallerMock = mock(FixerApiCaller.class);
	private ConversionRepository repositoryMock = mock(ConversionRepository.class);
	private ConversionService testObject = new ConversionService(repositoryMock, apiCallerMock);
	
	private static final Double API_RESULT = 7.5;
	private static final ConversionInputDto GENERIC_CONVERSION_INPUT = 
			new ConversionInputDto(5.0, "EUR", "USD");		
	
	@Test
	public void postConversion_ConversionSavedToDb_ConversionMappedCorrectly() {
		when(apiCallerMock.getApiResult(anyString(), anyString(), any(Double.class)))
			.thenReturn(API_RESULT);
		
		ArgumentCaptor<ConversionEntity> entityCaptor = ArgumentCaptor.forClass(ConversionEntity.class);
		
		testObject.postConversion(GENERIC_CONVERSION_INPUT);
		
		verify(repositoryMock, times(1)).save(entityCaptor.capture());
		ConversionEntity savedEntity = entityCaptor.getValue();
		
		compareConversionEntityToInputDto(savedEntity, GENERIC_CONVERSION_INPUT);
	}
	
	@Test
	public void postConversion_BeforeMethodReturn_ReturnValueMappedCorrectly() {
		when(apiCallerMock.getApiResult(anyString(), anyString(), any(Double.class)))
			.thenReturn(API_RESULT);
		
		ConversionDto returnValue = testObject.postConversion(GENERIC_CONVERSION_INPUT);
		
		compareConversionDtoToInputDto(returnValue, GENERIC_CONVERSION_INPUT);
	}
	
	@Test
	public void getConversions_DbReturnedConversionList_ElementsMappedCorrectly() {
		ConversionEntity dbAnswerConversion = ConversionMapper.INSTANCE.conversionInputDtoToConversion(GENERIC_CONVERSION_INPUT);
		dbAnswerConversion.setTargetAmount(API_RESULT);
				
		List<ConversionEntity> dbFindAllResponseContent = Collections.nCopies(2, dbAnswerConversion);
		
		when(repositoryMock.findAll(any(Pageable.class)))
			.thenReturn(new PageImpl<>(dbFindAllResponseContent));
		
		List<ConversionDto> returnList = testObject.getConversions(PageRequest.of(0, 2));
		
		assertNotNull(returnList);
		assertEquals(2, returnList.size());
		for (ConversionDto listElement : returnList) {
			compareConversionDtoToInputDto(listElement, GENERIC_CONVERSION_INPUT);
		}
		
	}
}
