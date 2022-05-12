package batu.springframework.exchangeapp.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.model.dtos.ExchangeRateDto;

@Service
public class ExchangeRateService {
	
	private final ServiceHelper serviceHelper;

	public ExchangeRateService(ServiceHelper serviceHelper) {
		this.serviceHelper = serviceHelper;
	}

	public ExchangeRateDto getExchangeRate(String source, String target) {
		BigDecimal rate = serviceHelper.calculateRate(source, target);
		
		return new ExchangeRateDto(source,target,rate);	
	}
}
