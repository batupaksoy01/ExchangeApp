package batu.springframework.exchangeapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.client.FixerApiCaller;
import batu.springframework.exchangeapp.dao.entity.ConversionEntity;
import batu.springframework.exchangeapp.dao.repository.ConversionRepository;
import batu.springframework.exchangeapp.mapper.ConversionMapper;
import batu.springframework.exchangeapp.model.dto.ConversionDto;
import batu.springframework.exchangeapp.model.dto.ConversionInputDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConversionService {
	
	private final ConversionRepository conversionRepository;
	private final FixerApiCaller apiCaller;
	
	public ConversionService(ConversionRepository conversionRepository, FixerApiCaller apiCaller) {
		this.conversionRepository = conversionRepository;
		this.apiCaller = apiCaller;
	}

	public ConversionDto postConversion(ConversionInputDto conversionInput) {
		log.info("postConversion method called");
		
		Double targetAmount = apiCaller.getApiResult(conversionInput.getSource(), conversionInput.getTarget(), conversionInput.getSourceAmount());
		
		ConversionEntity newConversion = ConversionMapper.INSTANCE.conversionInputDtoToConversion(conversionInput);
		newConversion.setTargetAmount(targetAmount);
		conversionRepository.save(newConversion);
	
		log.info("postConversion method returning");
		
		return ConversionMapper.INSTANCE.conversionToConversionDto(newConversion);
	}
	
	public List<ConversionDto> getConversions(Pageable pageable) {
		log.info("getConversions method called");
		
		Page<ConversionEntity> conversionPage = conversionRepository.findAll(pageable);
		List<ConversionEntity> conversionList = conversionPage.getContent();
		
		log.info("getConversions method returning");
		
		return ConversionMapper.INSTANCE.conversionListToConversionDtoList(conversionList);
	}
}
