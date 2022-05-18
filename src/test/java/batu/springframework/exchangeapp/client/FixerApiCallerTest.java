package batu.springframework.exchangeapp.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import batu.springframework.exchangeapp.model.dto.FixerResponseDto;
import batu.springframework.exchangeapp.exception.ApiException;
import batu.springframework.exchangeapp.exception.WrongInputException;

@RestClientTest(FixerApiCaller.class)
@AutoConfigureWebClient
public class FixerApiCallerTest {
	
	@Autowired
	private FixerApiCaller testObject;
    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper objectMapper;
	
	@Test
	public void getConversionResult_ApiReturnedResponse_ResponseCheckedAndReturned() {
		FixerResponseDto mockResponse = new FixerResponseDto(true, 5.0, null);
		
		setMockResponse(mockResponse);
		
		assertEquals(mockResponse.getResult(), testObject.getApiResult("", "", 1.0));
	}
	
	@Test
	public void getConversionResult_ApiReturnedEmptySuccessResponse_ApiExceptionThrown() throws JsonProcessingException {
		FixerResponseDto mockResponse = new FixerResponseDto();
		mockResponse.setSuccess(true);
		
		setMockResponse(mockResponse);
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.getApiResult("", "", 1.0);});
		assertEquals(exception, new ApiException());
	}
	
	@Test
	public void checkFixerResponse_ErrorMapIsNull_ResponseStatusExceptionThrown() {
		FixerResponseDto mockResponse = new FixerResponseDto();
		mockResponse.setSuccess(false);
		
		setMockResponse(mockResponse);
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.getApiResult("","",1.0);});
		assertEquals(exception, new ApiException());
	}
	
	@Test
	public void checkFixerResponse_ErrorMapDoesNotContainCode_ResponseStatusExceptionThrown() {
		FixerResponseDto mockResponse = new FixerResponseDto();
		mockResponse.setSuccess(false);
		mockResponse.setError(Map.of());
		
		setMockResponse(mockResponse);
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.getApiResult("","",1.0);});
		assertEquals(exception, new ApiException());
	}
	
	@Test
	public void checkFixerResponse_InvalidSourceCurrency_WrongInputExceptionThrown() {
		FixerResponseDto mockResponse = new FixerResponseDto();
		mockResponse.setSuccess(false);
		mockResponse.setError(Map.of("code","402", "type","invalid_from_currency"));
		
		setMockResponse(mockResponse);
		
		WrongInputException exception = assertThrows(WrongInputException.class, 
				() -> {testObject.getApiResult("","",1.0);});
		assertEquals(exception.getMessage(), "Source currency symbol isn't supported or multiple source currencies are provided.");
	}
	
	@Test
	public void checkFixerResponse_InvalidTargetCurrency_WrongInputExceptionThrown() {
		FixerResponseDto mockResponse = new FixerResponseDto();
		mockResponse.setSuccess(false);
		mockResponse.setError(Map.of("code","402", "type","invalid_to_currency"));
		
		setMockResponse(mockResponse);
		
		WrongInputException exception = assertThrows(WrongInputException.class, 
				() -> {testObject.getApiResult("","",1.0);});
		assertEquals(exception.getMessage(), "Target currency symbol isn't supported or multiple target currencies are provided.");
	}
	
	@Test
	public void checkFixerResponse_ErrorCode402TypeUnknown_ApiExceptionThrown() {
		FixerResponseDto mockResponse = new FixerResponseDto();
		mockResponse.setSuccess(false);
		mockResponse.setError(Map.of("code","402", "type","different_type"));
		
		setMockResponse(mockResponse);
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.getApiResult("","",1.0);});
		assertEquals(exception, new ApiException());
	}

	@Test
	public void checkFixerResponse_ErrorCodeIsOther_ResponseStatusExceptionThrown() {
		FixerResponseDto mockResponse = new FixerResponseDto();
		mockResponse.setSuccess(false);
		mockResponse.setError(Map.of("code","550"));
		
		setMockResponse(mockResponse);
		
		ApiException exception = assertThrows(ApiException.class, 
				() -> {testObject.getApiResult("","",1.0);});
		assertEquals(exception, new ApiException());
	}
	
	private void setMockResponse(FixerResponseDto mockResponse) {
		try {
			server.expect(anything()).andRespond(withSuccess(objectMapper.writeValueAsString(mockResponse), MediaType.APPLICATION_JSON));
		} catch (JsonProcessingException e) {
			fail("Given mockResponse couldn't be cast to JSON String");
		}
	}
}
