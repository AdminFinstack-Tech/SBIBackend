package com.csme.csmeapi.fin.config;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(JwtAuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<byte[]> handleJwtAuthenticationException(
			JwtAuthenticationException ex, HttpServletRequest request) throws IOException {
		String errorMessage = "Authentication failed: " + ex.getMessage();
		byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", errorMessage));

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(body);
	}

}

