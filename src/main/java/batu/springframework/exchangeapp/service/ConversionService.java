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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ConversionService {
	
	private final ConversionRepository conversionRepository;
	private final FixerApiCaller apiCaller;

	public ConversionDto postConversion(ConversionInputDto conversionInput) {	
		Double targetAmount = apiCaller.getApiResult(conversionInput.getSource(), conversionInput.getTarget(), conversionInput.getSourceAmount());
		
		ConversionEntity newConversion = ConversionMapper.INSTANCE.conversionInputDtoToConversion(conversionInput);
		newConversion.setTargetAmount(targetAmount);
		conversionRepository.save(newConversion);
		
		return ConversionMapper.INSTANCE.conversionToConversionDto(newConversion);
	}
	
	public List<ConversionDto> getConversions(Pageable pageable) {
		Page<ConversionEntity> conversionPage = conversionRepository.findAll(pageable);
		List<ConversionEntity> conversionList = conversionPage.getContent();
		
		return ConversionMapper.INSTANCE.conversionListToConversionDtoList(conversionList);
	}
}
