package batu.springframework.exchangeapp.service;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.data.dto.FixerResponseDTO;
import batu.springframework.exchangeapp.data.exception.WrongInputException;
import batu.springframework.exchangeapp.data.fixerApi.FixerApiCaller;

@Service
public class ServiceHelper {
	
	FixerApiCaller apiCaller;
	
	
	public ServiceHelper() {
		super();
		apiCaller = new FixerApiCaller();
	}

	public ServiceHelper(FixerApiCaller apiCaller) {
		super();
		this.apiCaller = apiCaller;
	}

	public Float calculateRate(String source, String target) {
		float targetRate;
		float sourceRate;
		FixerResponseDTO response = apiCaller.makeApiCall(source,target);
		try {
			targetRate = response.getRates().get(target);
		}	
		catch(Exception e) {
			throw new WrongInputException("Target currency symbol isn't supported or multiple target currencies are provided.");
		}
		try {
			 sourceRate = response.getRates().get(source);
		}	
		catch(Exception e) {
			throw new WrongInputException("Source currency symbol isn't supported or multiple source currencies are provided.");
		}
		return targetRate/sourceRate;
	}
}