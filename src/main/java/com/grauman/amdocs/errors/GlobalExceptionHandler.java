package com.grauman.amdocs.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler()
	protected ResponseEntity<Object> handler(RuntimeException ex, WebRequest request){
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ex.getMessage());
		//		return handleExceptionInternal(
//				ex, 
//				ex.getMessage(), 
//				new HttpHeaders(), 
//				HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
