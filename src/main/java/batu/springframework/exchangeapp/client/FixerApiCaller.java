package batu.springframework.exchangeapp.client;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import batu.springframework.exchangeapp.model.dto.FixerResponseDto;
import batu.springframework.exchangeapp.model.exception.ApiException;
import batu.springframework.exchangeapp.model.exception.WrongInputException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class FixerApiCaller {
	
	@Value("${fixerApiAccessKey}")
	private String apiAccessKey;
	@Value("${fixerApiUrl}")
	private String apiUrl;
	RestTemplate restTemplate = new RestTemplate();

	public Double getConversionResult(String source, String target, Double sourceAmount) {
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
