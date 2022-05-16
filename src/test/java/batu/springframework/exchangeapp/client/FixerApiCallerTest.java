package batu.springframework.exchangeapp.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
		FixerResponseDto mockResponse = new FixerResponseDto(true, 5.0, null);
		
		testObject.setRestTemplate(mockRestTemplate(mockResponse));
		
		assertEquals(mockResponse, testObject.getConversionResult("", "", 1.0));
		
		Mockito.verify(testObject, Mockito.times(1)).checkFixerResponse(Mockito.any(FixerResponseDto.class));
	}
	
	@Test
	public void getConversionResult_ApiReturnedEmptySuccessResponse_ApiExceptionThrown() {
		FixerResponseDto mockResponse = new FixerResponseDto();
		mockResponse.setSuccess(true);
		
		testObject.setRestTemplate(mockRestTemplate(mockResponse));
		
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
		RestTemplate restMock = Mockito.mock(RestTemplate.class);
		
		Mockito.when(restMock.exchange(
			    Mockito.anyString(),
			    Mockito.any(HttpMethod.class),
			    Mockito.any(HttpEntity.class),
			    Mockito.<Class<FixerResponseDto>>any(),
			    Mockito.<String,String>anyMap()))
				.thenReturn(new ResponseEntity<FixerResponseDto>(mockResponse, HttpStatus.OK));
		
		return restMock;
	}
}
