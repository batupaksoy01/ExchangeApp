package batu.springframework.exchangeapp.service;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.model.dto.ExchangeRateDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExchangeRateService {
	
	private final FixerApiCaller apiCaller;

	public ExchangeRateDto getExchangeRate(String source, String target) {
		Double rate = apiCaller.getApiResult(source, target, 1.0);
		
		return new ExchangeRateDto(source,target,rate);	
	}
}
