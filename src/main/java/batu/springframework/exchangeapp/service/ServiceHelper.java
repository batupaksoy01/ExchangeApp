package batu.springframework.exchangeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import batu.springframework.exchangeapp.data.dto.FixerResponseDTO;
import batu.springframework.exchangeapp.data.exception.WrongInputException;
import batu.springframework.exchangeapp.data.fixerApi.fixerApiCaller;
import batu.springframework.exchangeapp.data.model.Conversion;
import batu.springframework.exchangeapp.data.repository.ConversionRepository;

@Service
public class ServiceHelper {
	public Float calculateRate(String source, String target) {
		float targetRate;
		float sourceRate;
		FixerResponseDTO response = fixerApiCaller.makeApiCall(source,target);
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