package batu.springframework.exchangeapp.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.spy;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import batu.springframework.exchangeapp.model.dto.FixerResponseDto;
import batu.springframework.exchangeapp.model.exception.ApiException;
import batu.springframework.exchangeapp.model.exception.WrongInputException;

public class FixerApiCallerTest {

	private FixerApiCaller testObject = new FixerApiCaller();
	
	@Test
	public void getConversionResult_ApiReturnedResponse_ResponseCheckedAndReturned() {
		testObject = spy(FixerApiCaller.class);
		
		FixerResponseDto mockResponse = new FixerResponseDto(true, 5.0, null);
		
		testObject.setRestTemplate(mockRestTemplate(mockResponse));
		initializeApiParams();
		
		assertEquals(mockResponse.getResult(), testObject.getConversionResult("", "", 1.0));
		
		verify(testObject, times(1)).checkFixerResponse(any(FixerResponseDto.class));
	}
	
	@Test
	public void getConversionResult_ApiReturnedEmptySuccessResponse_ApiExceptionThrown() {
		FixerResponseDto mockResponse = new FixerResponseDto();
		mockResponse.setSuccess(true);
		
		testObject.setRestTemplate(mockRestTemplate(mockResponse));
		initializeApiParams();
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.getConversionResult("", "", 1.0);});
		assertEquals(exception, new ApiException());
	}
	
	@Test
	public void checkFixerResponse_ResponseSuccessfull_ReturnedWithoutException() {
		FixerResponseDto testInput = new FixerResponseDto();
		testInput.setSuccess(true);
		
		try {
			testObject.checkFixerResponse(testInput);
		} catch (WrongInputException|ApiException e) {
			fail("Exception was thrown");
		}
	}
	
	@Test
	public void checkFixerResponse_ErrorMapIsNull_ResponseStatusExceptionThrown() {
		FixerResponseDto testInput = new FixerResponseDto();
		testInput.setSuccess(false);
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.checkFixerResponse(testInput);});
		assertEquals(exception, new ApiException());
	}
	
	@Test
	public void checkFixerResponse_ErrorMapDoesNotContainCode_ResponseStatusExceptionThrown() {
		FixerResponseDto testInput = new FixerResponseDto();
		testInput.setSuccess(false);
		testInput.setError(Map.of());
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.checkFixerResponse(testInput);});
		assertEquals(exception, new ApiException());
	}
	
	@Test
	public void checkFixerResponse_InvalidSourceCurrency_WrongInputExceptionThrown() {
		FixerResponseDto testInput = new FixerResponseDto();
		testInput.setSuccess(false);
		testInput.setError(Map.of("code","402", "type","invalid_from_currency"));
		
		WrongInputException exception = assertThrows(WrongInputException.class, 
				() -> {testObject.checkFixerResponse(testInput);});
		assertEquals(exception.getMessage(), "Source currency symbol isn't supported or multiple source currencies are provided.");
	}
	
	@Test
	public void checkFixerResponse_InvalidTargetCurrency_WrongInputExceptionThrown() {
		FixerResponseDto testInput = new FixerResponseDto();
		testInput.setSuccess(false);
		testInput.setError(Map.of("code","402", "type","invalid_to_currency"));
		
		WrongInputException exception = assertThrows(WrongInputException.class, 
				() -> {testObject.checkFixerResponse(testInput);});
		assertEquals(exception.getMessage(), "Target currency symbol isn't supported or multiple target currencies are provided.");
	}
	
	@Test
	public void checkFixerResponse_ErrorCode402TypeUnknown_ApiExceptionThrown() {
		FixerResponseDto testInput = new FixerResponseDto();
		testInput.setSuccess(false);
		testInput.setError(Map.of("code","402", "type","different_type"));
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.checkFixerResponse(testInput);});
		assertEquals(exception, new ApiException());
	}

	@Test
	public void checkFixerResponse_ErrorCodeIsOther_ResponseStatusExceptionThrown() {
		FixerResponseDto testInput = new FixerResponseDto();
		testInput.setSuccess(false);
		testInput.setError(Map.of("code","550"));
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.checkFixerResponse(testInput);});
		assertEquals(exception, new ApiException());
	}
	
	private RestTemplate mockRestTemplate(FixerResponseDto mockResponse) {
		RestTemplate restMock = mock(RestTemplate.class);
		
		when(restMock.exchange(
			    anyString(),
			    any(HttpMethod.class),
			    any(HttpEntity.class),
			    Mockito.<Class<FixerResponseDto>>any(),
			    Mockito.<String,String>anyMap()))
				.thenReturn(new ResponseEntity<FixerResponseDto>(mockResponse, HttpStatus.OK));
		
		return restMock;
	}
	
	private void initializeApiParams() {
		testObject.setApiAccessKey("");
		testObject.setApiUrl("");
	}
}
