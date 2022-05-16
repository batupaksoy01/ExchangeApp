package batu.springframework.exchangeapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.dao.entity.ConversionEntity;
import batu.springframework.exchangeapp.dao.repository.ConversionRepository;
import batu.springframework.exchangeapp.model.dto.ConversionDto;
import batu.springframework.exchangeapp.model.dto.ConversionInputDto;
import batu.springframework.exchangeapp.model.mapper.ConversionMapper;

@Service
public class ConversionService {
	
	private final ConversionRepository conversionRepository;
	private final FixerApiCaller apiCaller;
	
	public ConversionService(ConversionRepository conversionRepository, FixerApiCaller apiCaller) {
		this.conversionRepository = conversionRepository;
		this.apiCaller = apiCaller;
	}

	public ConversionDto postConversion(ConversionInputDto conversionInput) {
		Double targetAmount = apiCaller.getConversionResult(conversionInput.getSource(), conversionInput.getTarget(), conversionInput.getSourceAmount());
		
		ConversionEntity newConversion = ConversionMapper.INSTANCE.conversionInputDtoToConversion(conversionInput);
		newConversion.setTargetAmount(targetAmount);
		conversionRepository.save(newConversion);
	
		return ConversionMapper.INSTANCE.conversionToConversionDto(newConversion);
	}
	
	public List<ConversionDto> getConversions(Pageable pageable) {
		Page<ConversionEntity> conversionPage = conversionRepository.findAll(pageable);
		List<ConversionEntity> conversionList = conversionPage.getContent();
		
		return conversionList.stream().map(conversion -> 
			ConversionMapper.INSTANCE.conversionToConversionDto(conversion)).collect(Collectors.toList());
	}
}
