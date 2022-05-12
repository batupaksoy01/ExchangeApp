package batu.springframework.exchangeapp.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.model.dtos.FixerResponseDto;
import batu.springframework.exchangeapp.model.exceptions.WrongInputException;

@Service
public class ServiceHelper {
	
	private final FixerApiCaller apiCaller;

	public ServiceHelper(FixerApiCaller apiCaller) {
		this.apiCaller = apiCaller;
	}

	public BigDecimal calculateRate(String source, String target) {
		FixerResponseDto response = apiCaller.makeApiCall(source,target);
		
		BigDecimal targetRate = getCurrencyRate(target, response, "target");
		BigDecimal sourceRate = getCurrencyRate(source, response, "source");
		
		return targetRate.divide(sourceRate);
	}
	
	private BigDecimal getCurrencyRate(String currencyName, FixerResponseDto response, String currencyType) {
		try {
			return response.getRates().get(currencyName);
		}	
		catch(Exception e) {
			throw new WrongInputException(currencyType.substring(0, 1).toUpperCase() + currencyType.substring(1) + 
					" currency symbol isn't supported or multiple " + currencyType + " currencies are provided.");
		}
	}
}