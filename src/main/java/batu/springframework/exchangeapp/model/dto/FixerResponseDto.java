package batu.springframework.exchangeapp.model.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixerResponseDto {
		private boolean success = false;
		private Double result;
		private Map<String,String> error;
}
