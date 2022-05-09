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

import batu.springframework.exchangeapp.data.dto.FailResponseDTO;
import batu.springframework.exchangeapp.data.exception.ApiException;
import batu.springframework.exchangeapp.data.exception.WrongInputException;

@ControllerAdvice
public class ConversionControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(WrongInputException.class) 
	public ResponseEntity<FailResponseDTO> handleWrongInput(WrongInputException e){
		return new ResponseEntity<FailResponseDTO>(new FailResponseDTO(e.getErrorMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ApiException.class) 
	public ResponseEntity<FailResponseDTO> handleApiException(ApiException e){
		return new ResponseEntity<FailResponseDTO>(new FailResponseDTO(e.getErrorMessage()), HttpStatus.BAD_GATEWAY);
	}
	
	@ExceptionHandler(ConstraintViolationException.class) 
	public ResponseEntity<FailResponseDTO> handleConstraintViolationException(ConstraintViolationException e){
		return new ResponseEntity<FailResponseDTO>(new FailResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResponseStatusException.class) 
	public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e){
		return new ResponseEntity<Object>(null, e.getStatus());
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<Object>(new FailResponseDTO("Http request method not supported."), HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String endpoint = request.getDescription(false);
		if(endpoint.equals("uri=/api/exchangeRate")) {
			String missingParam = ex.getParameterName();
			missingParam = missingParam.substring(0, 1).toUpperCase() + missingParam.substring(1);
			return new ResponseEntity<Object>(new FailResponseDTO(missingParam + " currency is missing."), HttpStatus.NOT_FOUND);
		}
		/*if(endpoint.equals("uri=/api/getConversions")) {
			String missingParam = ex.getParameterName();
			missingParam = missingParam.substring(0, 1).toUpperCase() + missingParam.substring(1);
			return new ResponseEntity<Object>(new FailResponseDTO(missingParam + " parameter is required."), HttpStatus.NOT_FOUND);
		}*/
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

	    return new ResponseEntity<Object>(new FailResponseDTO(String.join(" ", errors)), HttpStatus.BAD_REQUEST);
	}
	
	
}
