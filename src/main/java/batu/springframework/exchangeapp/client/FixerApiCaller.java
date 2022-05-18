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
		HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiAccessKey);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        
        Map<String, String> uriParams = Map.of(
        		"source", source,
        		"target", target,
        		"sourceAmount", sourceAmount.toString());
        
		FixerResponseDto response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, FixerResponseDto.class, uriParams).getBody();
		
		checkFixerResponse(response);
		
		return Optional.ofNullable(response.getResult()).
				orElseThrow(() -> new ApiException());
	}
	
	protected void checkFixerResponse(FixerResponseDto response) {
		if (response.isSuccess()) {
			log.info("checkfixerResponse.start: response successfull");
			return;	
		}
		
		try {
			Map<String, String> errorMap = response.getError();
			
			if (errorMap.get("code").equals("402")) {
				
				String errorType = errorMap.get("type");
				if (errorType.equals("invalid_from_currency")) {
					log.info("checkfixerResponse.end: WrongInputException thrown");
					throw new WrongInputException("Source currency symbol isn't supported or multiple source currencies are provided.");
				}
				else if (errorType.equals("invalid_to_currency")) {
					log.info("checkfixerResponse.end: WrongInputException thrown");
					throw new WrongInputException("Target currency symbol isn't supported or multiple target currencies are provided.");
				}
				
				else {
					log.error("checkfixerResponse.end: api error= " + errorMap.get("info"));
					throw new ApiException();
				}
			}	
		} catch(NullPointerException e) {
			log.error("checkfixerResponse.end: api error is in unexpected format");
			throw new ApiException();
		}
	}
}
