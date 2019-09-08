package com.grauman.amdocs.errors;

import com.grauman.amdocs.errors.custom.GeneralError;
import com.grauman.amdocs.errors.custom.InvalidCredentials;
import com.grauman.amdocs.errors.custom.InvalidDataException;
import com.grauman.amdocs.errors.custom.ResultsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private ResponseEntity<Object> generateError(HttpStatus status, String message){
		return ResponseEntity.status(status)
				.body(message);
	}

	@ExceptionHandler({SQLException.class, NullPointerException.class, IndexOutOfBoundsException.class})
	protected ResponseEntity<Object> handleInternalErrors(RuntimeException ex, WebRequest request){
		return generateError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}



	@ExceptionHandler({ResultsNotFoundException.class})
	protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request){
		return generateError(HttpStatus.NOT_FOUND, ex.getMessage());



	}

	@ExceptionHandler({InvalidDataException.class, InvalidCredentials.class})
	protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request){
		return generateError(HttpStatus.UNAUTHORIZED, ex.getMessage());

	}


}
