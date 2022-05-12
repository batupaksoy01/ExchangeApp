package batu.springframework.exchangeapp.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.dao.entities.Conversion;
import batu.springframework.exchangeapp.dao.repositories.ConversionRepository;
import batu.springframework.exchangeapp.data.mappers.ConversionMapper;
import batu.springframework.exchangeapp.model.dtos.ConversionDto;
import batu.springframework.exchangeapp.model.dtos.ConversionInputDto;

@Service
public class ConversionService {
	
	private final ConversionRepository conversionRepository;
	private final ServiceHelper serviceHelper;
	
	public ConversionService(ConversionRepository conversionRepository, ServiceHelper serviceHelper) {
		this.conversionRepository = conversionRepository;
		this.serviceHelper = serviceHelper;
	}

	public ConversionDto getConversion(ConversionInputDto conversionInput) {
		BigDecimal rate = serviceHelper.calculateRate(conversionInput.getSource(), conversionInput.getTarget());
		
		Conversion newConversion = ConversionMapper.INSTANCE.conversionInputDtoToConversion(conversionInput);
		newConversion.setTargetAmount( rate.multiply(newConversion.getSourceAmount()) );
		conversionRepository.save(newConversion);
	
		return ConversionMapper.INSTANCE.conversionToConversionDto(newConversion);
	}
	
	public List<ConversionDto> getConversions(Pageable pageable) {
		Page<Conversion> conversionPage = conversionRepository.findAll(pageable);
		List<Conversion> conversionList = conversionPage.getContent();
		
		return conversionList.stream().map(conversion -> 
			ConversionMapper.INSTANCE.conversionToConversionDto(conversion)).collect(Collectors.toList());
	}
}
