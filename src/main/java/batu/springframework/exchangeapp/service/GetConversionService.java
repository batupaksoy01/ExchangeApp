package batu.springframework.exchangeapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.dto.ConversionDTO;
import batu.springframework.exchangeapp.dto.ConversionInputDTO;
import batu.springframework.exchangeapp.dto.SuccessResponseDTO;
import batu.springframework.exchangeapp.exception.ApiException;
import batu.springframework.exchangeapp.models.Conversion;

@Service
public class GetConversionService {
	private ServiceHelper serviceHelper;
	
	public GetConversionService(ServiceHelper serviceHelper) {
		super();
		this.serviceHelper = serviceHelper;
	}

	public SuccessResponseDTO getConversion(ConversionInputDTO conversionInput) {
		float rate = serviceHelper.calculateRate(conversionInput.getSource(), conversionInput.getTarget());
		if (rate > 0) {
			Conversion newConversion = new Conversion();
			BeanUtils.copyProperties(conversionInput, newConversion);
			newConversion.setTargetAmount( rate * conversionInput.getSourceAmount() );
			newConversion.setDateTime(LocalDateTime.now());
			serviceHelper.saveConversion(newConversion);
			List<ConversionDTO> conversionList = new ArrayList<ConversionDTO>();
			ConversionDTO conversionDTO = new ConversionDTO();
			BeanUtils.copyProperties(newConversion, conversionDTO);
			conversionList.add(conversionDTO);
			return new SuccessResponseDTO(conversionList);
		}
		else {
			throw new ApiException(serviceHelper.mapError(rate));
		}
	}

}
