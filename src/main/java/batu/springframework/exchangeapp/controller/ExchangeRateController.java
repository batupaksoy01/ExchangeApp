package batu.springframework.exchangeapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import batu.springframework.exchangeapp.model.dto.ExchangeRateDto;
import batu.springframework.exchangeapp.service.ExchangeRateService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {
	
	private final ExchangeRateService exchangeRateService;
	
	public ExchangeRateController(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	@GetMapping("/exchange-rate")
	@ApiOperation(value="Get a specific exchange rate between two currencies.", 
	notes="Provide a source and target currency to receive the exchange rate between the two.", response=ExchangeRateDto.class)
	public ExchangeRateDto getExchangeRate(@RequestParam("source")  String source, @RequestParam("target")  String target) {
		return exchangeRateService.getExchangeRate(source, target);
	}
}
