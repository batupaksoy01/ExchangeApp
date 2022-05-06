package batu.springframework.exchangeapp.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponseDTO extends ResponseDTO{
	private List<ConversionDTO> conversionList;
	
	

	@Override
	public String toString() {
		return "Response: [success=" + isSuccess()+ ", conversion=" + conversionList + "]";
	}



	@Override
	public boolean isSuccess() {
		return true;
	}

	

}
