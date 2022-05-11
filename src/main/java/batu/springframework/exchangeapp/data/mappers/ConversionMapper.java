package batu.springframework.exchangeapp.data.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import batu.springframework.exchangeapp.data.dtos.ConversionDto;
import batu.springframework.exchangeapp.data.dtos.ConversionInputDto;
import batu.springframework.exchangeapp.data.models.Conversion;

@Mapper
public interface ConversionMapper {
	ConversionMapper INSTANCE = Mappers.getMapper( ConversionMapper.class );
	 
    ConversionDto conversionToConversionDto(Conversion conversion);
    Conversion conversionDtoToConversion(ConversionDto conversionDto);
    Conversion conversionInputDtoToConversion(ConversionInputDto conversion);
}
