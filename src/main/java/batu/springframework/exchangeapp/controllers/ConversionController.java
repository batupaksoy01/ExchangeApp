package batu.springframework.exchangeapp.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import batu.springframework.exchangeapp.data.dto.ConversionInputDTO;
import batu.springframework.exchangeapp.data.dto.SuccessResponseDTO;
import batu.springframework.exchangeapp.service.ExchangeRateService;
import batu.springframework.exchangeapp.service.GetConversionService;
import batu.springframework.exchangeapp.service.GetConversionsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Validated
@RestController
@RequestMapping("/api")
public class ConversionController {
	@Autowired
	private ExchangeRateService exchangeRateService;
	
	@Autowired
	private GetConversionService getConversionService;
	
	@Autowired
	private GetConversionsService getConversionsService;
	

	public ConversionController(ExchangeRateService exchangeRateService, GetConversionService getConversionService,
			GetConversionsService getConversionsService) {
		super();
		this.exchangeRateService = exchangeRateService;
		this.getConversionService = getConversionService;
		this.getConversionsService = getConversionsService;
	}

	@PostMapping("/getConversion")
	@ApiOperation(value="Create a currency conversion in the database.", 
	notes="Include a full ConversionInputDTO in the request body to save the conversion.", response=SuccessResponseDTO.class)
	ResponseEntity<SuccessResponseDTO> getConversion(@Valid @RequestBody @ApiParam(required=true) ConversionInputDTO newConversion) {
		return new ResponseEntity<SuccessResponseDTO>(getConversionService.getConversion(newConversion), HttpStatus.OK);
	}
	
	@GetMapping("/getConversions")
	@ApiOperation(value="Get all currency conversions in the database.", 
	notes="Provide page number and size to receive the previous conversions in the page.", response=SuccessResponseDTO.class)
	ResponseEntity<SuccessResponseDTO> getConversions(@RequestParam("page") @Positive(message="Page parameter must be positive.") @NotNull @ApiParam(required=true) int page, 
			@RequestParam("size") @Positive(message="Size parameter must be positive.") @NotNull @ApiParam(required=true) int size) {
		return new ResponseEntity<SuccessResponseDTO>(getConversionsService.getConversions(page,size), HttpStatus.OK);
	}
	
	@GetMapping("/exchangeRate")
	@ApiOperation(value="Get a specific exchange rate between two currencies.", 
	notes="Provide a source and target currency to receive the exchange rate between the two.", response=SuccessResponseDTO.class)
	ResponseEntity<SuccessResponseDTO> exchangeRate(@RequestParam("source") @NotBlank(message="Source parameter is required.") @ApiParam(required=true) String source, 
			@RequestParam("target") @NotBlank(message="Target parameter is required.") @ApiParam(required=true) String target) {
		return new ResponseEntity<SuccessResponseDTO>(exchangeRateService.getExchangeRate(source, target), HttpStatus.OK);
	}
}
