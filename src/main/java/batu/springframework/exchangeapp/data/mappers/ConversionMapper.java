package batu.springframework.exchangeapp.data.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import batu.springframework.exchangeapp.dao.entities.Conversion;
import batu.springframework.exchangeapp.model.dtos.ConversionDto;
import batu.springframework.exchangeapp.model.dtos.ConversionInputDto;

@Mapper
public interface ConversionMapper {
	ConversionMapper INSTANCE = Mappers.getMapper( ConversionMapper.class );
	 
    ConversionDto conversionToConversionDto(Conversion conversion);
    Conversion conversionDtoToConversion(ConversionDto conversionDto);
    Conversion conversionInputDtoToConversion(ConversionInputDto conversion);
}
