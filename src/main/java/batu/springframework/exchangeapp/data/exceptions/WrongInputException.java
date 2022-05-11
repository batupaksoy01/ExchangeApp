package batu.springframework.exchangeapp.data.exceptions;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class WrongInputException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String errorMessage;
}
