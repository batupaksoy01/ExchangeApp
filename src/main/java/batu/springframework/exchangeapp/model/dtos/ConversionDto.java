package batu.springframework.exchangeapp.model.dtos;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionDto {
	private String source;
	private String target;
	private BigDecimal sourceAmount;
	private BigDecimal targetAmount;	
}
