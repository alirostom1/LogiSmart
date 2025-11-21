package io.github.alirostom1.logismart.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefreshRequest{
    @NotBlank(message = "Refresh token is required!")
    private String refreshToken;
}
