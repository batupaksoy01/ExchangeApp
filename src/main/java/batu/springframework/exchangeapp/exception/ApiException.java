package batu.springframework.exchangeapp.exception;

import java.util.Objects;

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
public class ApiException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String message = "The server failed to process your request, please try another time or try other endpoints";
	
	@Override
	public boolean equals(Object obj) {
		ApiException other = (ApiException) obj;
		return Objects.equals(message, other.message);
	}
}
