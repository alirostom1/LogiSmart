package io.github.alirostom1.logismart.dto.request.courier;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourierRequest {
    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "First name is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 32,message = "Password should be between 6 and 32 characters")
    private String password;

    @NotBlank(message = "Vehicle is required")
    private String vehicle;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotNull(message = "Zone ID is required")
    private Long zoneId;
}