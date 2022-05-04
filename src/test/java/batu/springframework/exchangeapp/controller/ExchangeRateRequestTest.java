package batu.springframework.exchangeapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import batu.springframework.exchangeapp.controllers.ConversionController;
import batu.springframework.exchangeapp.service.ExchangeRateService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ExchangeRateRequestTest {
	@Mock
	ExchangeRateService serviceMock;
	@InjectMocks
	ConversionController testObject;
	@Autowired
	private MockMvc mockMvc;
	
	//Valid input
	@Test
	public void whenInputIsValid_thenServiceIsCalled() {
		Mockito.when(serviceMock.getExchangeRate(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		try {
			mockMvc.perform(get("/api/exchangeRate?source=USD&target=GBP")).andDo(print()).andExpect(status().isOk());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Mockito.verify(serviceMock.getExchangeRate("USD", "GBP"), Mockito.times(1));
	}
	
	//Parameter missing
	@Test
	public void whenParamIsMissing_thenExceptionThrowh() {
		
	}
	
	//Parameter duplicate
	@Test
	public void whenParamIsDuplicate_ServiceIsCalled() {
		
	}
}
