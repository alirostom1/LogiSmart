package io.github.alirostom1.logismart.advice;


import io.github.alirostom1.logismart.dto.response.common.ApiResponse;

import io.github.alirostom1.logismart.exception.PostalCodeAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleValidationErrors(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ApiResponse<List<String>> apiResponse = new ApiResponse<>(
                false,
                "Request body validation error",
                errors,
                System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleHandlerMethodValidation(HandlerMethodValidationException ex){
        List<String> errors = new ArrayList<>();
        ex.getParameterValidationResults().forEach(validationResult ->{
            String parameterName = validationResult.getMethodParameter().getParameterName();
            validationResult.getResolvableErrors().forEach(error ->{
                String errorMessage = parameterName + ": " + error.getDefaultMessage();
                errors.add(errorMessage);
            });
        });
        ApiResponse<List<String>> apiResponse = new ApiResponse<>(
                false,
                "Validation failed in request params or path variable!",
                errors,
                System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(apiResponse);
    }

    //CUSTOM EXCEPTIONS
    @ExceptionHandler(PostalCodeAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostalCodeDuplication(PostalCodeAlreadyExistsException ex){
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex){
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
