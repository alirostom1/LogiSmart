package io.github.alirostom1.logismart.dto.response.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultApiResponse<T>{
    private boolean success;
    private String message;
    private T data;
    private long timestamp;
}
