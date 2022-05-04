package batu.springframework.exchangeapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.dto.ConversionDTO;
import batu.springframework.exchangeapp.dto.SuccessResponseDTO;
import batu.springframework.exchangeapp.exception.ApiException;

@Service
public class ExchangeRateService {
	private ServiceHelper serviceHelper;
	
	public ExchangeRateService() {
		super();
	}

	public ExchangeRateService(ServiceHelper serviceHelper) {
		super();
		this.serviceHelper = serviceHelper;
	}

	public SuccessResponseDTO getExchangeRate(String source, String target) {
		float rate = serviceHelper.calculateRate(source, target);
		if(rate > 0) {
			List<ConversionDTO> conversionList = new ArrayList<ConversionDTO>();
			conversionList.add(new ConversionDTO(source,target,1,rate,LocalDateTime.now()));
			return new SuccessResponseDTO(conversionList);
		}
		else {
			throw new ApiException(serviceHelper.mapError(rate));
		}
	}
}