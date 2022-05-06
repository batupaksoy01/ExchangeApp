package batu.springframework.exchangeapp.data.fixerApi;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import batu.springframework.exchangeapp.data.dto.FixerResponseDTO;
import batu.springframework.exchangeapp.data.exception.ApiException;

public class fixerApiCaller {
	private static final String API_ACCESS_KEY = "df9cabc38b212ae8ad94f522e1407f96";
	
	public static FixerResponseDTO makeApiCall(String source, String target) {
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://data.fixer.io/api/latest")
				.queryParam("access_key", API_ACCESS_KEY)
				.queryParam("symbols", source + ',' + target);
		throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
		/*FixerResponseDTO response = restTemplate.getForObject(builder.toUriString(), FixerResponseDTO.class);
		checkFixerResponse(response);
		return response;*/
	}
	
	public static void checkFixerResponse(FixerResponseDTO response) {
		if(!response.isSuccess()) {
			String errorCode;
			try {
				errorCode = response.getError().get("code");
			} catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
			}
			if (errorCode.equals("202")) {
				throw new ApiException("One or more currency symbols are not supported.");
			}
			if (errorCode.equals("404")) {
				throw new ApiException("The requested resource does not exist.");
			}
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
		}	
	}
}
