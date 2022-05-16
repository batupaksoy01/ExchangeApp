package batu.springframework.exchangeapp.service;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.model.dto.ExchangeRateDto;

@Service
public class ExchangeRateService {
	
	private final FixerApiCaller apiCaller;

	public ExchangeRateService(FixerApiCaller apiCaller) {
		this.apiCaller = apiCaller;
	}

	public ExchangeRateDto getExchangeRate(String source, String target) {
		Double rate = apiCaller.getConversionResult(source, target, 1.0);
		
		return new ExchangeRateDto(source,target,rate);	
	}
}
