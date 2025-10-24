package io.github.alirostom1.logismart.advice;


import io.github.alirostom1.logismart.exception.CourierNotFoundException;
import io.github.alirostom1.logismart.exception.DeliveryNotFoundException;
import io.github.alirostom1.logismart.exception.InvalidUUIDException;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.dto.ApiResponse;
import io.github.alirostom1.logismart.dto.ApiResponseError;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError> handleValidationErrors(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ApiResponseError response = new ApiResponseError(
                400,
                "Validation failed,Please check your input.",
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() == UUID.class) {
            String invalidValue = ex.getValue() != null ? ex.getValue().toString() : "null";
            ApiResponse response = new ApiResponse(
                    400,
                    "Invalid UUID format: " + invalidValue
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if(ex.getRequiredType() == DeliveryStatus.class){
            String invalidValue = ex.getValue() != null ? ex.getValue().toString() : "null";
            ApiResponse response = new ApiResponse(
                    400,
                    "Invalid status format(PREPARATION,IN_TRANSIT,DELIVERED): " + invalidValue
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }


        ApiResponse response = new ApiResponse(
                400,
                "Invalid parameter format: " + ex.getName()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }



    //CUSTOM EXCEPTIONS


    @ExceptionHandler(CourierNotFoundException.class)
    public ResponseEntity<ApiResponse> handleCourierNotFound(CourierNotFoundException ex){
        ApiResponse response = new ApiResponse(
                404,
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handlePhoneAlreadyExists(PhoneAlreadyExistsException ex){
        ApiResponse response = new ApiResponse(
                400,
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<ApiResponse> handleDeliveryNotFound(DeliveryNotFoundException ex){
        ApiResponse response = new ApiResponse(
                404,
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
       //DEBUG
        ex.printStackTrace();

        ApiResponse response = new ApiResponse(
                500,
                "Internal Server Error. Please try again later."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
