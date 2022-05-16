package batu.springframework.exchangeapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionDto {
	private String source;
	private String target;
	private Double sourceAmount;
	private Double targetAmount;
}
