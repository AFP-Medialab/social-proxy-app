package com.afp.medialab.weverify.socialproxy.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		if (ex instanceof NullPointerException) {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		} else if (ex instanceof NotConnectedException) {
			ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
			return new ResponseEntity<Object>(errorResponse, HttpStatus.FORBIDDEN);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}

class ErrorResponse {
	
	public ErrorResponse(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
