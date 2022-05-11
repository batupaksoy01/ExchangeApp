package batu.springframework.exchangeapp.services;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.data.dtos.FixerResponseDto;
import batu.springframework.exchangeapp.data.exceptions.WrongInputException;
import batu.springframework.exchangeapp.external.FixerApiCaller;

@Service
public class ServiceHelper {
	
	private FixerApiCaller apiCaller;

	public ServiceHelper(FixerApiCaller apiCaller) {
		this.apiCaller = apiCaller;
	}

	public Float calculateRate(String source, String target) {
		FixerResponseDto response = apiCaller.makeApiCall(source,target);
		
		Float targetRate = getCurrencyRate(target, response, "target");
		Float sourceRate = getCurrencyRate(source, response, "source");
		
		return targetRate/sourceRate;
	}
	
	private Float getCurrencyRate(String currencyName, FixerResponseDto response, String currencyType) {
		try {
			return response.getRates().get(currencyName);
		}	
		catch(Exception e) {
			throw new WrongInputException(currencyType.substring(0, 1).toUpperCase() + currencyType.substring(1) + 
					" currency symbol isn't supported or multiple " + currencyType + " currencies are provided.");
		}
	}
}