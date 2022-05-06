package batu.springframework.exchangeapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.data.dto.ConversionDTO;
import batu.springframework.exchangeapp.data.dto.SuccessResponseDTO;
import batu.springframework.exchangeapp.data.model.Conversion;
import batu.springframework.exchangeapp.data.repository.ConversionRepository;

@Service
public class GetConversionsService {
	@Autowired
	private ConversionRepository conversionRepository;
	private ServiceHelper serviceHelper;
	
	public GetConversionsService(ServiceHelper serviceHelper) {
		super();
		this.serviceHelper = serviceHelper;
	}
	
	public SuccessResponseDTO getConversions(int page, int size) {
		List<ConversionDTO> conversionDTOList = new ArrayList<ConversionDTO>();
		Pageable pageable = PageRequest.of(page, size);
		Page<Conversion> conversionPage = conversionRepository.findAll(pageable);
		List<Conversion> conversionList = conversionPage.getContent();
		for(Conversion conversion: conversionList) {
			ConversionDTO conversionDTO = new ConversionDTO();
			BeanUtils.copyProperties(conversion, conversionDTO);
			conversionDTOList.add(conversionDTO);
		}
		return new SuccessResponseDTO(conversionDTOList);
	}

}
