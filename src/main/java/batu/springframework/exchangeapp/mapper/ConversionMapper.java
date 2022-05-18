package batu.springframework.exchangeapp.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import batu.springframework.exchangeapp.dao.entity.ConversionEntity;
import batu.springframework.exchangeapp.model.dto.ConversionDto;
import batu.springframework.exchangeapp.model.dto.ConversionInputDto;

@Mapper(componentModel = "spring")
public interface ConversionMapper {
	ConversionMapper INSTANCE = Mappers.getMapper( ConversionMapper.class );
	 
    ConversionDto conversionToConversionDto(ConversionEntity conversion);
    ConversionEntity conversionInputDtoToConversion(ConversionInputDto conversion);
    List<ConversionDto> conversionListToConversionDtoList(List<ConversionEntity> conversionEntityList); 
}
