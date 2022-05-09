package batu.springframework.exchangeapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.data.dto.ConversionDTO;
import batu.springframework.exchangeapp.data.dto.SuccessResponseDTO;

@Service
public class ExchangeRateService {
	private ServiceHelper serviceHelper;

	public ExchangeRateService(ServiceHelper serviceHelper) {
		super();
		this.serviceHelper = serviceHelper;
	}

	public SuccessResponseDTO getExchangeRate(String source, String target) {
		float rate = serviceHelper.calculateRate(source, target);
		List<ConversionDTO> conversionList = new ArrayList<ConversionDTO>();
		conversionList.add(new ConversionDTO(source,target,1,rate,LocalDateTime.now()));
		return new SuccessResponseDTO(conversionList);
	}
}
