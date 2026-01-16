package io.github.alirostom1.logismart.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        DefaultApiResponse<Object> apiResponse = DefaultApiResponse.builder()
                .success(false)
                .message("Unauthorized: " + authException.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
