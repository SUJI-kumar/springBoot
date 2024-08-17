package com.prth.irest.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handles unsupported HTTP method exceptions and returns a 405 status. Provides
	 * a user-friendly message indicating the unsupported HTTP method.
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		// Log the warning message
		pageNotFoundLogger.warn(ex.getMessage());

		// Create a custom error message
		String errorMessage = "The HTTP method " + ex.getMethod() + " is not supported for this endpoint.";

		// Return a custom response with 405 status code
		return handleExceptionInternal(ex, errorMessage, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
	}

	/**
	 * Handles cases where no handler is found for a request and returns a 404
	 * status. Provides a user-friendly message indicating the resource was not
	 * found.
	 */
//	@Override
//	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
//			HttpStatusCode status, WebRequest request) {
//		// Create a custom error message
//		String errorMessage = "The requested resource was not found. Please check the URL and try again.";
//		return handleExceptionInternal(ex, errorMessage, headers, HttpStatus.NOT_FOUND, request);
//	}

	/**
	 * Handles type mismatch errors in method arguments and returns a 400 status.
	 * Provides a message specifying the invalid input and the expected type.
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		String errorMessage = String.format("Invalid input: '%s' is not a valid %s.", ex.getValue(),
				ex.getRequiredType().getSimpleName());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}

	 /**
     * Handles validation errors when arguments are not valid and returns a 400 status.
     * Extracts and formats validation errors from the MethodArgumentNotValidException.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        // Create a map to hold field errors
        Map<String, String> errors = new LinkedHashMap<>();
        
        // Extract field errors from the exception
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("errors", errors);
        body.put("message", "Validation failed for one or more fields.");

        // Return the custom error response
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }
    
    /**
     * Handles JSON mapping exceptions by returning a 400 Bad Request response.
     * Provides an error message indicating invalid or unrecognized fields in the request body.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // Extract the detailed message from the cause if it's a JsonMappingException
        String specificDetail = "Failed to read request body";
        if (ex.getCause() instanceof JsonMappingException jsonMappingException) {
            specificDetail = extractSpecificDetail(jsonMappingException);
        }

        // Customize the problem detail structure
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, specificDetail);
        body.setTitle("JSON Mapping Error");
        URI typeUri = URI.create("https://example.com/problem-details/json-mapping-error");
        body.setType(typeUri);
        body.setInstance(typeUri);//optional

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Extracts a more specific detail message from a JsonMappingException,
     * including all unrecognized fields in the request body.
     */
    private String extractSpecificDetail(JsonMappingException ex) {
        List<String> unrecognizedFields = new ArrayList<>();

        for (JsonMappingException.Reference reference : ex.getPath()) {
            unrecognizedFields.add("'" + reference.getFieldName() + "'");
        }

        if (!unrecognizedFields.isEmpty()) {
            return "Unrecognized fields in the request body: " + String.join(", ", unrecognizedFields);
        }
        return "Unrecognized fields in the request body";
    }	


}
