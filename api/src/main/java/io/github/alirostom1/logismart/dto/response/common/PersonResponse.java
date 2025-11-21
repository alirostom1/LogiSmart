package io.github.alirostom1.logismart.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonResponse {
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
}
