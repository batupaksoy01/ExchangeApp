package batu.springframework.exchangeapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import batu.springframework.exchangeapp.dto.ConversionDTO;
import batu.springframework.exchangeapp.dto.SuccessResponseDTO;
import batu.springframework.exchangeapp.models.Conversion;

@Service
public class GetConversionsService {
	private ServiceHelper serviceHelper;
	
	public GetConversionsService(ServiceHelper serviceHelper) {
		super();
		this.serviceHelper = serviceHelper;
	}
	
	public SuccessResponseDTO getConversions(int page, int size) {
		List<ConversionDTO> conversionDTOList = new ArrayList<ConversionDTO>();
		if (page > 0) {
			page -= 1;
		}
		Pageable pageable = PageRequest.of(page, size);
		Page<Conversion> conversionPage = serviceHelper.getConversionsByPage(pageable);
		List<Conversion> conversionList = conversionPage.getContent();
		for(Conversion conversion: conversionList) {
			ConversionDTO conversionDTO = new ConversionDTO();
			BeanUtils.copyProperties(conversion, conversionDTO);
			conversionDTOList.add(conversionDTO);
		}
		return new SuccessResponseDTO(conversionDTOList);
	}

}
