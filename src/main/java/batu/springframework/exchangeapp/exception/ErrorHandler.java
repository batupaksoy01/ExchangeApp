package batu.springframework.exchangeapp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import batu.springframework.exchangeapp.model.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(WrongInputException.class) 
	public ResponseEntity<Object> handleWrongInput(WrongInputException exception, WebRequest request){
		log.info("Handling WrongInputException: message= " + exception.getMessage());
		
		ErrorDto errorDto = new ErrorDto(400, "invalid_currency", exception.getMessage());
		
		return handleExceptionInternal(exception, errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(ApiException.class) 
	public ResponseEntity<Object> handleApiException(ApiException exception, WebRequest request){
		log.info("Handling ApiException: message= " + exception.getMessage());
		
		ErrorDto errorDto = new ErrorDto(502, "server_problem", exception.getMessage());
		
		return handleExceptionInternal(exception, errorDto, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = ex.getParameterName() + " request parameter is required.";
		ErrorDto errorDto = new ErrorDto(400, "missing_request_parameter", message);
		
		log.info("Handling MissingServletRequestParameterException: message= " + message);
		
		return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.BAD_GATEWAY, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = "Request body is missing or it is not readable.";
		ErrorDto errorDto = new ErrorDto(400, "invalid_request_body", message);
		
		log.info("Handling HttpMessageNotReadableException: message= " + message);
		
		return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = ex.getFieldErrors().get(0).getDefaultMessage();
		ErrorDto errorDto = new ErrorDto(400, "invalid_request_body", message);
		
		log.info("Handling MethodArgumentNotValidException: message= " + message);
		
		return handleExceptionInternal(ex, errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}	
}
