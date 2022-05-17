package batu.springframework.exchangeapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import batu.springframework.exchangeapp.ErrorDtoChecker;
import batu.springframework.exchangeapp.model.dto.ConversionInputDto;
import batu.springframework.exchangeapp.model.dto.ErrorDto;
import batu.springframework.exchangeapp.model.exception.ApiException;
import batu.springframework.exchangeapp.model.exception.WrongInputException;
import batu.springframework.exchangeapp.service.ConversionService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ConversionController.class)
public class ConversionControllerTest {
	
	@Autowired
	private MockMvc mvc;
	@MockBean
	ConversionService serviceMock;
	
	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	
	String path = "/api/conversions";
	
	@Test
	public void postConversion_RequestBodyMissing_ExceptionThrown() throws Exception {
		String jsonResponse = mvc.perform(post(path)).andReturn().getResponse().getContentAsString();
		ErrorDto errorDtoResponse = new ObjectMapper().readValue(jsonResponse, ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(errorDtoResponse, 400, "invalid_request_body", "Request body is missing or it is not readable.");
	}
	
	@Test
	public void postConversion_RequestBodyNotParseable_ExceptionThrown() throws Exception {
		String jsonResponse = mvc.perform(post(path)
				.contentType(MediaType.APPLICATION_JSON).content("{\"source\": \"EUR\",, \"target\": \"USD\", \"sourceAmount\": 100}"))
				.andReturn().getResponse().getContentAsString();
		ErrorDto errorDtoResponse = new ObjectMapper().readValue(jsonResponse, ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(errorDtoResponse, 400, "invalid_request_body", "Request body is missing or it is not readable.");
	}
	
	@Test
	public void postConversion_sourceAmountNegative_ExceptionThrown() throws Exception {
		String jsonResponse = mvc.perform(post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(new ConversionInputDto(-100.0, "EUR", "USD"))))
				.andReturn().getResponse().getContentAsString();
		ErrorDto errorDtoResponse = new ObjectMapper().readValue(jsonResponse, ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(errorDtoResponse, 400, "invalid_request_body", "sourceAmount must be positive.");
	}
	
	@Test
	public void postConversion_RequestBodyValid_ServiceCalled() throws Exception {
		mvc.perform(post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(new ConversionInputDto(100.0, "EUR", "USD"))))
				.andReturn();
		
		verify(serviceMock).postConversion(new ConversionInputDto(100.0, "EUR", "USD"));
	}
	
	@Test
	public void postConversion_ServiceThrewWrongInputException_CatchedByErrorHandler() throws Exception {
		when(serviceMock.postConversion(any(ConversionInputDto.class))).thenThrow(new WrongInputException("exception message"));
		
		String jsonResponse = mvc.perform(post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(new ConversionInputDto(100.0, "EUR", "USD"))))
				.andReturn().getResponse().getContentAsString();
		ErrorDto errorDtoResponse = new ObjectMapper().readValue(jsonResponse, ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(errorDtoResponse, 400, "invalid_currency", "exception message");
	}
	
	@Test
	public void postConversion_ServiceThrewApiException_CatchedByErrorHandler() throws Exception {
		when(serviceMock.postConversion(any(ConversionInputDto.class))).thenThrow(new ApiException());
		
		String jsonResponse = mvc.perform(post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(ow.writeValueAsString(new ConversionInputDto(100.0, "EUR", "USD"))))
				.andReturn().getResponse().getContentAsString();
		ErrorDto errorDtoResponse = new ObjectMapper().readValue(jsonResponse, ErrorDto.class);
		
		ErrorDtoChecker.checkErrorDto(errorDtoResponse, 502, "server_problem", "The server failed to process your request, please try another time or try other endpoints");
	}
	
	@Test
	public void getConversions_PageableGiven_ServiceCalled() throws Exception {
		mvc.perform(get(path));
		
		verify(serviceMock).getConversions(any(Pageable.class));
	}
	
	@Test
	public void getConversions_PageableNotGiven_ServiceCalled() throws Exception {
		mvc.perform(get(path + "?page=1&size=2&sort=id,DESC"));
		
		verify(serviceMock).getConversions(PageRequest.of(1, 2, Sort.by("id").descending()));
	}
}
