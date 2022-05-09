package batu.springframework.exchangeapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import batu.springframework.exchangeapp.data.dto.ConversionDTO;
import batu.springframework.exchangeapp.data.model.Conversion;
import batu.springframework.exchangeapp.data.repository.ConversionRepository;

public class GetConversionsTest {
	
	ConversionRepository repositoryMock = Mockito.mock(ConversionRepository.class);
	GetConversionsService testObject = new GetConversionsService(repositoryMock);
	
	@Test
	public void givenPageAndSize_thenDatabaseIsCalled() {
		Mockito.when(repositoryMock.findAll(PageRequest.of(2, 3))).thenReturn(new PageImpl<>(new ArrayList<>()));
		testObject.getConversions(2, 3);
		Mockito.verify(repositoryMock, Mockito.times(1)).findAll(PageRequest.of(2, 3));
	}
	
	@Test
	public void whenDatabaseAnswers_thenDataTransformedAndReturned() {
		Pageable pageable = PageRequest.of(2, 3);
		List<Conversion> responseList = new ArrayList<>();
		responseList.add(new Conversion((long) 1,(float)5,(float)7.5,"EUR","USD",LocalDateTime.now()));
		responseList.add(new Conversion((long) 1,(float)3,(float)15,"USD","TRY",LocalDateTime.now()));
		Mockito.when(repositoryMock.findAll(pageable)).thenReturn(new PageImpl<>(responseList, pageable, responseList.size()));
		List<ConversionDTO> returnList = testObject.getConversions(2, 3).getConversionList();
		Assertions.assertEquals(responseList.size(), returnList.size());
		for(int i = 0; i < responseList.size(); i++) {
			Assertions.assertEquals(responseList.get(i).getSource(), returnList.get(i).getSource());
			Assertions.assertEquals(responseList.get(i).getTarget(), returnList.get(i).getTarget());
			Assertions.assertEquals(responseList.get(i).getSourceAmount(), returnList.get(i).getSourceAmount());
			Assertions.assertEquals(responseList.get(i).getTargetAmount(), returnList.get(i).getTargetAmount());
			Assertions.assertEquals(responseList.get(i).getDateTime(), returnList.get(i).getDateTime());
		}
	}
}
