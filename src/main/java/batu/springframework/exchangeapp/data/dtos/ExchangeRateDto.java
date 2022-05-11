package batu.springframework.exchangeapp.data.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDto {
	private String source;
	private String target;
	private Float rate;
}
