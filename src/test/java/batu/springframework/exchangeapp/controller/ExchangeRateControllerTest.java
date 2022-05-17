
package batu.springframework.exchangeapp.controller;

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

import batu.springframework.exchangeapp.ErrorDtoChecker;
import batu.springframework.exchangeapp.model.dto.ErrorDto;
import batu.springframework.exchangeapp.service.ExchangeRateService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTest {
	
	@Autowired
	private MockMvc mvc;
	@MockBean
	ExchangeRateService serviceMock;
	
	@Test
	public void getExchangeRate_SourceMissing_ErrorDtoReturned() throws Exception  {
		String path = "/api/exchange-rate?target=USD";
		
		ErrorDto response = sendInvalidRequest(path);
		ErrorDtoChecker.checkErrorDto(response, 400, "missing_request_parameter", "source request parameter is required.");
	}
	
	@Test
	public void getExchangeRate_TargetMissing_ErrorDtoReturned() throws Exception {
		String path = "/api/exchange-rate?source=EUR";
		
		ErrorDto response = sendInvalidRequest(path);
		ErrorDtoChecker.checkErrorDto(response, 400, "missing_request_parameter", "target request parameter is required.");
	}
	
	@Test
	public void getExchangeRate_RequestParamsValid_Service() throws Exception {
		mvc.perform(get("/api/exchange-rate?source=USD&target=EUR")).andReturn();
		
		Mockito.verify(serviceMock).getExchangeRate("USD", "EUR");
	}
	
	private ErrorDto sendInvalidRequest(String path) throws Exception {
		String response = mvc.perform(get(path)).andReturn().getResponse().getContentAsString();
		System.out.println(response);
		return new ObjectMapper().readValue(response, ErrorDto.class);
	}
}
