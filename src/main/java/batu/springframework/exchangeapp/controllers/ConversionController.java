package batu.springframework.exchangeapp.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import batu.springframework.exchangeapp.model.dtos.ConversionDto;
import batu.springframework.exchangeapp.model.dtos.ConversionInputDto;
import batu.springframework.exchangeapp.services.ConversionService;
import io.swagger.annotations.ApiOperation;

@Validated
@RestController
@RequestMapping("/api")
public class ConversionController {
	
	private final ConversionService conversionService;

	public ConversionController(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@PostMapping("/conversions")
	@ApiOperation(value="Create a currency conversion in the database.", 
	notes="Include a full ConversionInputDTO in the request body to save the conversion.", response=ConversionDto.class)
	public ConversionDto getConversion(@Valid @RequestBody ConversionInputDto newConversion) {
		return conversionService.getConversion(newConversion);
	}
	
	@GetMapping("/conversions")
	@ApiOperation(value="Get all currency conversions in the database.", 
	notes="Provide page number and size to receive the previous conversions in the page.", response=List.class)
	public List<ConversionDto> getConversions(@RequestParam Pageable pageable) {
		return conversionService.getConversions(pageable);
	}
}
