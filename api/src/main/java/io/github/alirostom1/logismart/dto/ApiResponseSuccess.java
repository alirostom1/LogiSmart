package io.github.alirostom1.logismart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseSuccess<T> extends ApiResponse{
    private T data;

    public ApiResponseSuccess(int status, String message, T data) {
        super(status,message);
        this.data = data;
    }
}
