package batu.springframework.exchangeapp.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionDTO {
	private String source;
	private String target;
	private float sourceAmount;
	private float targetAmount;
	private LocalDateTime dateTime;
	
}
