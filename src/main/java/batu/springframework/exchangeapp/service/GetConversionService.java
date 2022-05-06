package batu.springframework.exchangeapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.data.dto.ConversionDTO;
import batu.springframework.exchangeapp.data.dto.ConversionInputDTO;
import batu.springframework.exchangeapp.data.dto.SuccessResponseDTO;
import batu.springframework.exchangeapp.data.exception.ApiException;
import batu.springframework.exchangeapp.data.model.Conversion;
import batu.springframework.exchangeapp.data.repository.ConversionRepository;

@Service
public class GetConversionService {
	@Autowired
	private ConversionRepository conversionRepository;
	private ServiceHelper serviceHelper;
	
	public GetConversionService(ServiceHelper serviceHelper) {
		super();
		this.serviceHelper = serviceHelper;
	}

	public SuccessResponseDTO getConversion(ConversionInputDTO conversionInput) {
		float rate = serviceHelper.calculateRate(conversionInput.getSource(), conversionInput.getTarget());
		Conversion newConversion = new Conversion();
		BeanUtils.copyProperties(conversionInput, newConversion);
		newConversion.setTargetAmount( rate * conversionInput.getSourceAmount() );
		newConversion.setDateTime(LocalDateTime.now());
		conversionRepository.save(newConversion);
		List<ConversionDTO> conversionList = new ArrayList<ConversionDTO>();
		ConversionDTO conversionDTO = new ConversionDTO();
		BeanUtils.copyProperties(newConversion, conversionDTO);
		conversionList.add(conversionDTO);
		return new SuccessResponseDTO(conversionList);
	}

}
