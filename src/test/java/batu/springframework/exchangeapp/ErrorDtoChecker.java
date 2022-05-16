package batu.springframework.exchangeapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import batu.springframework.exchangeapp.model.dto.ErrorDto;

public class ErrorDtoChecker {
	public static void checkErrorDto(ErrorDto errorDto, Integer status, String error, String message) {
		assertNotNull(errorDto);
		assertEquals(status, errorDto.getStatus());
		assertEquals(error, errorDto.getError());
		assertEquals(message, errorDto.getMessage());
	}
}
