package batu.springframework.exchangeapp.model.dtos;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversionInputDto {
	@NotNull(message="sourceAmount field is required.")
	@Positive(message="sourceAmount must be positive.")
	private BigDecimal sourceAmount;
	@NotBlank(message="source field is required.")
	private String source;
	@NotBlank(message="target field is required.")
	private String target;
}
