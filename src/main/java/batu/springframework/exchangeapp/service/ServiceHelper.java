package batu.springframework.exchangeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import batu.springframework.exchangeapp.FixerResponse;
import batu.springframework.exchangeapp.exception.WrongInputException;
import batu.springframework.exchangeapp.models.Conversion;
import batu.springframework.exchangeapp.repositories.ConversionRepository;

@Service
public class ServiceHelper {
	
	@Autowired
	private ConversionRepository conversionRepository;
	private final String API_ACCESS_KEY = "df9cabc38b212ae8ad94f522e1407f96";
	
	protected ServiceHelper() {
		super();
	}

	public ServiceHelper(ConversionRepository conversionRepository) {
		super();
		this.conversionRepository = conversionRepository;
	}

	public Page<Conversion> getConversionsByPage(Pageable pageable) {
		return conversionRepository.findAll(pageable);
	}
	
	public void saveConversion(Conversion conversion) {
		conversionRepository.save(conversion);
	}
	
	protected Float calculateRate(String source, String target) {
		float targetRate;
		float sourceRate;
		FixerResponse response = makeApiCall(source,target);
		if(response.isSuccess()) {
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
		return (-1)* Float.parseFloat(response.getError().get("code"));
	}
	
	protected FixerResponse makeApiCall(String source, String target) {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://data.fixer.io/api/latest")
				.queryParam("access_key", API_ACCESS_KEY)
				.queryParam("symbols", source + ',' + target);
		return restTemplate.getForObject(builder.toUriString(), FixerResponse.class);
	}
	
	public String mapError(Float errorCode) {
		if (Float.compare(errorCode, (float)-100) < 0 && Float.compare(errorCode, (float)-200) > 0) {
			return "Requested endpoint can't be reached, please try another endpoint or different parameters.";
		}
		if (Float.compare(errorCode, (float)-202) == 0) {
			return "One or more currency symbols are not supported.";
		}
		if (Float.compare(errorCode, (float)-404) == 0) {
			return "The requested resource does not exist.";
		}
		return "An unknown error accurred. Couldn't reach currency data.";
	}

	public ConversionRepository getConversionRepository() {
		return conversionRepository;
	}

	public void setConversionRepository(ConversionRepository conversionRepository) {
		this.conversionRepository = conversionRepository;
	}
}