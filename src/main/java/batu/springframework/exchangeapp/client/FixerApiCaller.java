package batu.springframework.exchangeapp.client;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import batu.springframework.exchangeapp.exception.ApiException;
import batu.springframework.exchangeapp.exception.WrongInputException;
import batu.springframework.exchangeapp.model.dto.FixerResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FixerApiCaller {
	
	@Value("${fixerApiAccessKey}")
	private String apiAccessKey;
	@Value("${fixerApiUrl}")
	private String apiUrl;
	private final RestTemplate restTemplate;
	
	public FixerApiCaller(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public Double getApiResult(String source, String target, Double sourceAmount) {
		log.info("getConversionResult method called");
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiAccessKey);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        
        Map<String, String> uriParams = Map.of(
        		"source", source,
        		"target", target,
        		"sourceAmount", sourceAmount.toString());
        
		FixerResponseDto response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, FixerResponseDto.class, uriParams).getBody();
		
		checkFixerResponse(response);
		
		log.info("getConversionResult method returning");
		
		return Optional.ofNullable(response.getResult()).
				orElseThrow(() -> new ApiException());
	}
	
	protected void checkFixerResponse(FixerResponseDto response) {
		if (response.isSuccess()) {
			log.info("response is checked and it is successfull");
			return;	
		}
		
		try {
			if (response.getError().get("code").equals("402")) {
				String errorType = response.getError().get("type");
				if (errorType.equals("invalid_from_currency")) {
					throw new WrongInputException("Source currency symbol isn't supported or multiple source currencies are provided.");
				}
				else if (errorType.equals("invalid_to_currency")) {
					throw new WrongInputException("Target currency symbol isn't supported or multiple target currencies are provided.");
				}
			}	
		} catch(NullPointerException e) {
			throw new ApiException();
		}
		
		throw new ApiException();
	}
}
