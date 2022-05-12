package batu.springframework.exchangeapp.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import batu.springframework.exchangeapp.model.dtos.FixerResponseDto;
import batu.springframework.exchangeapp.model.exceptions.ApiException;

@Component
public class FixerApiCaller {
	
	@Value("${fixerApiAccessKey}")
	private String apiAccessKey;
	@Value("${fixerApiBaseUrl}")
	private String apiBaseUrl;
	
	public FixerResponseDto makeApiCall(String source, String target) {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl)
				.queryParam("access_key", apiAccessKey)
				.queryParam("symbols", source + ',' + target);
		FixerResponseDto response = restTemplate.getForObject(builder.toUriString(), FixerResponseDto.class);
		
		checkFixerResponse(response);
		
		return response;
	}
	
	public void checkFixerResponse(FixerResponseDto response) {
		if(response.isSuccess()) {
			return;
		}
		String errorCode;
		try {
			errorCode = response.getError().get("code");
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
		}
		if (errorCode.equals("202")) {
			throw new ApiException("One or more currency symbols are not supported.");
		}
		else if (errorCode.equals("404")) {
			throw new ApiException("The requested resource does not exist.");
		}
		throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
	}
}
