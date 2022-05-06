package batu.springframework.exchangeapp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FailResponseDTO extends ResponseDTO{
	private String errorMessage = "An unknown error accurred.";
	
	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public String toString() {
		return "Response: [success=" + isSuccess() + ", errorMessage=" + errorMessage + "]";
	}
	
	
}
