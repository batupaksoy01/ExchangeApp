package batu.springframework.exchangeapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.model.dto.ExchangeRateDto;

@Service
public class ExchangeRateService {
	
	private final FixerApiCaller apiCaller;
	
	private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateService.class);

	public ExchangeRateService(FixerApiCaller apiCaller) {
		this.apiCaller = apiCaller;
	}

	public ExchangeRateDto getExchangeRate(String source, String target) {
		LOG.info("getExchangeRate method called");
		
		Double rate = apiCaller.getConversionResult(source, target, 1.0);
		
		LOG.info("getExchangeRate method returning");
		
		return new ExchangeRateDto(source,target,rate);	
	}
}
