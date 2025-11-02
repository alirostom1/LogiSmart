package io.github.alirostom1.logismart.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private String address;
}
