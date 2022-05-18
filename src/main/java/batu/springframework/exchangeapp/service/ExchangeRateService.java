package batu.springframework.exchangeapp.service;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.model.dto.ExchangeRateDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExchangeRateService {
	
	private final FixerApiCaller apiCaller;

	public ExchangeRateService(FixerApiCaller apiCaller) {
		this.apiCaller = apiCaller;
	}

	public ExchangeRateDto getExchangeRate(String source, String target) {
		log.info("getExchangeRate method called");
		
		Double rate = apiCaller.getApiResult(source, target, 1.0);
		
		log.info("getExchangeRate method returning");
		
		return new ExchangeRateDto(source,target,rate);	
	}
}
