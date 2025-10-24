package io.github.alirostom1.logismart.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ApiResponseError extends ApiResponse{
    private List<String> errors;

    public ApiResponseError(int status,String message,List<String> errors){
        super(status,message);
        this.errors = errors;
    }
}
