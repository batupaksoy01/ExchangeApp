package batu.springframework.exchangeapp.exception;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WrongInputException extends RuntimeException {
	private static final long serialVersionUID = 2L;
	private String message;
	
	@Override
	public boolean equals(Object obj) {
		WrongInputException other = (WrongInputException) obj;
		return Objects.equals(message, other.message);
	}
}
