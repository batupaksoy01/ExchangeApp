package batu.springframework.exchangeapp.testUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import batu.springframework.exchangeapp.dao.entity.ConversionEntity;
import batu.springframework.exchangeapp.model.dto.ConversionDto;
import batu.springframework.exchangeapp.model.dto.ConversionInputDto;

public interface ConversionComparator {
	default String getConversionResourcePath(int port) {
		return "http://localhost:" + port + "/api/conversions";
	}
	
	default void compareConversionDtoToEntity(ConversionDto returned, ConversionEntity expected) {
		assertNotNull(returned);
		assertEquals(expected.getSource(),returned.getSource());
		assertEquals(expected.getTarget(),returned.getTarget());
		assertEquals(expected.getSourceAmount(),returned.getSourceAmount());
		assertEquals(expected.getTargetAmount(), returned.getTargetAmount());
	}
	
	default void compareConversionEntityToInputDto(ConversionEntity returned, ConversionInputDto expected) {
		assertNotNull(returned);
		assertEquals(expected.getSource(),returned.getSource());
		assertEquals(expected.getTarget(),returned.getTarget());
		assertEquals(expected.getSourceAmount(),returned.getSourceAmount());
		assertNotNull(returned.getTargetAmount());
	}
	
	default void compareConversionDtoToInputDto(ConversionDto returned, ConversionInputDto expected) {
		assertNotNull(returned);
		assertEquals(expected.getSource(),returned.getSource());
		assertEquals(expected.getTarget(),returned.getTarget());
		assertEquals(expected.getSourceAmount(),returned.getSourceAmount());
		assertNotNull(returned.getTargetAmount());
	}
}
