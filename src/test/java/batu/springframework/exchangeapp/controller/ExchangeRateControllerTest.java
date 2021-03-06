
package batu.springframework.exchangeapp.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import batu.springframework.exchangeapp.exception.ApiException;
import batu.springframework.exchangeapp.exception.WrongInputException;
import batu.springframework.exchangeapp.model.dto.ErrorDto;
import batu.springframework.exchangeapp.service.ExchangeRateService;
import batu.springframework.exchangeapp.testUtil.ErrorDtoChecker;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTest {
	
	@Autowired
	private MockMvc mvc;
	@MockBean
	private ExchangeRateService serviceMock;
	
	@Test
	public void getExchangeRate_SourceMissing_ErrorDtoReturned() throws Exception  {
		String path = "/api/exchange-rate?target=USD";
		
		ErrorDto response = sendRequestForErrorDto(path);
		ErrorDtoChecker.checkErrorDto(response, 400, "missing_request_parameter", "source request parameter is required.");
	}
	
	@Test
	public void getExchangeRate_TargetMissing_ErrorDtoReturned() throws Exception {
		String path = "/api/exchange-rate?source=EUR";
		
		ErrorDto response = sendRequestForErrorDto(path);
		ErrorDtoChecker.checkErrorDto(response, 400, "missing_request_parameter", "target request parameter is required.");
	}
	
	@Test
	public void getExchangeRate_RequestParamsValid_Service() throws Exception {
		String path = "/api/exchange-rate?source=USD&target=EUR";
		
		mvc.perform(get(path)).andReturn();
		
		Mockito.verify(serviceMock).getExchangeRate("USD", "EUR");
	}
	
	@Test
	public void getExchangeRate_ServiceThrewWrongInputException_CatchedByErrorHandler() throws Exception {
		when(serviceMock.getExchangeRate(anyString(), anyString())).thenThrow(new WrongInputException("exception message"));
		
		String path = "/api/exchange-rate?source=USD&target=EUR";
		
		ErrorDto response = sendRequestForErrorDto(path); 
		
		ErrorDtoChecker.checkErrorDto(response, 400, "invalid_currency", "exception message");
	}
	
	@Test
	public void getExchangeRate_ServiceThrewApiException_CatchedByErrorHandler() throws Exception {
		when(serviceMock.getExchangeRate(anyString(), anyString())).thenThrow(new ApiException());
		
		String path = "/api/exchange-rate?source=USD&target=EUR";
		
		ErrorDto response = sendRequestForErrorDto(path);
		
		ErrorDtoChecker.checkErrorDto(response, 502, "server_problem", "The server failed to process your request, please try another time or try other endpoints");
	}
	
	private ErrorDto sendRequestForErrorDto(String path) throws Exception {
		String response = mvc.perform(get(path)).andReturn().getResponse().getContentAsString();
		
		return new ObjectMapper().readValue(response, ErrorDto.class);
	}
}
