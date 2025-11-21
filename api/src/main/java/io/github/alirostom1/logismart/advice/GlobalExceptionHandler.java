package io.github.alirostom1.logismart.advice;


import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;

import io.github.alirostom1.logismart.exception.PostalCodeAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultApiResponse<List<String>>> handleValidationErrors(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        DefaultApiResponse<List<String>> defaultApiResponse = new DefaultApiResponse<>(
                false,
                "Request body validation error",
                errors,
                System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(defaultApiResponse);
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<DefaultApiResponse<List<String>>> handleHandlerMethodValidation(HandlerMethodValidationException ex){
        List<String> errors = new ArrayList<>();
        ex.getParameterValidationResults().forEach(validationResult ->{
            String parameterName = validationResult.getMethodParameter().getParameterName();
            validationResult.getResolvableErrors().forEach(error ->{
                String errorMessage = parameterName + ": " + error.getDefaultMessage();
                errors.add(errorMessage);
            });
        });
        DefaultApiResponse<List<String>> defaultApiResponse = new DefaultApiResponse<>(
                false,
                "Validation failed in request params or path variable!",
                errors,
                System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(defaultApiResponse);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DefaultApiResponse<Void>> handleAuthenticationException(AuthenticationException ex){
        DefaultApiResponse<Void> apiResponse = new DefaultApiResponse<>(
                false,
                "Invalid credentials!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    //CUSTOM EXCEPTIONS
    @ExceptionHandler(PostalCodeAlreadyExistsException.class)
    public ResponseEntity<DefaultApiResponse<Void>> handlePostalCodeDuplication(PostalCodeAlreadyExistsException ex){
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                false,
                ex.getMessage(),
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(defaultApiResponse);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DefaultApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex){
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                false,
                ex.getMessage(),
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(defaultApiResponse);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DefaultApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        DefaultApiResponse<String> response = new DefaultApiResponse<>(
                false,
                ex.getMessage(),
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultApiResponse<String>> handleGenericException(Exception ex) {
        ex.printStackTrace();
        DefaultApiResponse<String> response = new DefaultApiResponse<>(
                false,
                "An unexpected error occurred",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
