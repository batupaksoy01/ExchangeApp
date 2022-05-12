package batu.springframework.exchangeapp.advice;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import batu.springframework.exchangeapp.model.exceptions.ApiException;
import batu.springframework.exchangeapp.model.exceptions.WrongInputException;

@ControllerAdvice
public class ConversionControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(WrongInputException.class) 
	public ResponseEntity<String> handleWrongInput(WrongInputException exception){
		return new ResponseEntity<String>(exception.getErrorMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ApiException.class) 
	public ResponseEntity<String> handleApiException(ApiException exception){
		return new ResponseEntity<String>(exception.getErrorMessage(), HttpStatus.BAD_GATEWAY);
	}
	
	@ExceptionHandler(ConstraintViolationException.class) 
	public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exception){
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResponseStatusException.class) 
	public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException exception){
		return new ResponseEntity<Object>(null, exception.getStatus());
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<Object>("Http request method not supported.", HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String endpoint = request.getDescription(false);
		if(endpoint.equals("uri=/api/exchange-rate")) {
			String missingParam = ex.getParameterName();
			missingParam = missingParam.substring(0, 1).toUpperCase() + missingParam.substring(1);
			return new ResponseEntity<Object>(missingParam + " currency is missing.", HttpStatus.NOT_FOUND);
		}
		return super.handleMissingServletRequestParameter(ex,headers,status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

	    List<String> errors = ex.getBindingResult()
	        .getFieldErrors()
	        .stream()
	        .map(DefaultMessageSourceResolvable::getDefaultMessage)
	        .collect(Collectors.toList());

	    return new ResponseEntity<Object>(String.join(" ", errors), HttpStatus.BAD_REQUEST);
	}
	
	
}
