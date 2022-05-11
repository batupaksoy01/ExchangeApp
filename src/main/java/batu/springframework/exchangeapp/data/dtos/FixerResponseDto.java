package batu.springframework.exchangeapp.data.dtos;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixerResponseDto {
		private boolean success = false;
		private Long timestamp;
		private Date date;
		private Map<String,Float> rates;
		private Map<String,String> error;		
}
