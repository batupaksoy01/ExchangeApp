package batu.springframework.exchangeapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger LOG = LoggerFactory.getLogger(ConversionService.class);
	
	public ConversionService(ConversionRepository conversionRepository, FixerApiCaller apiCaller) {
		this.conversionRepository = conversionRepository;
		this.apiCaller = apiCaller;
	}

	public ConversionDto postConversion(ConversionInputDto conversionInput) {
		LOG.info("postConversion method called");
		
		Double targetAmount = apiCaller.getConversionResult(conversionInput.getSource(), conversionInput.getTarget(), conversionInput.getSourceAmount());
		
		ConversionEntity newConversion = ConversionMapper.INSTANCE.conversionInputDtoToConversion(conversionInput);
		newConversion.setTargetAmount(targetAmount);
		conversionRepository.save(newConversion);
	
		LOG.info("postConversion method returning");
		
		return ConversionMapper.INSTANCE.conversionToConversionDto(newConversion);
	}
	
	public List<ConversionDto> getConversions(Pageable pageable) {
		LOG.info("getConversions method called");
		
		Page<ConversionEntity> conversionPage = conversionRepository.findAll(pageable);
		List<ConversionEntity> conversionList = conversionPage.getContent();
		
		LOG.info("getConversions method returning");
		
		return conversionList.stream().map(conversion -> 
			ConversionMapper.INSTANCE.conversionToConversionDto(conversion)).collect(Collectors.toList());
	}
}
