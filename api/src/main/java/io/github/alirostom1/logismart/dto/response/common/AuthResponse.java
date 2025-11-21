package io.github.alirostom1.logismart.dto.response.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse{
    private TokenPair tokenPair;
    private PersonResponse personResponse;
    private String role;
}
