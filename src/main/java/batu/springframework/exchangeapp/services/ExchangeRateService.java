package batu.springframework.exchangeapp.services;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.data.dtos.ExchangeRateDto;

@Service
public class ExchangeRateService {
	
	private ServiceHelper serviceHelper;

	public ExchangeRateService(ServiceHelper serviceHelper) {
		this.serviceHelper = serviceHelper;
	}

	public ExchangeRateDto getExchangeRate(String source, String target) {
		Float rate = serviceHelper.calculateRate(source, target);
		
		return new ExchangeRateDto(source,target,rate);	
	}
}
