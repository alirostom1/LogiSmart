package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.LoginRequest;
import io.github.alirostom1.logismart.dto.request.RefreshRequest;
import io.github.alirostom1.logismart.dto.request.person.RegisterRequest;
import io.github.alirostom1.logismart.dto.response.common.AuthResponse;
import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
import io.github.alirostom1.logismart.dto.response.common.TokenPair;
import io.github.alirostom1.logismart.model.entity.Sender;
import io.github.alirostom1.logismart.service.AuthService;
import io.github.alirostom1.logismart.service.JWTService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/auth")
public class AuthController {
    private final AuthService authService;

    //FOR ALL ACTORS EXCEPT RECIPIENT
    @PostMapping("/login")
    public ResponseEntity<DefaultApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        DefaultApiResponse apiResonse = DefaultApiResponse.builder()
                .success(true)
                .message("Succesfully logged in!")
                .data(response)
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.ok(apiResonse);
    }


    //ONLY FOR RECIPIENT
    @PostMapping("/register")
    public ResponseEntity<DefaultApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        DefaultApiResponse<AuthResponse> apiResponse = DefaultApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Succesfully registered!")
                .data(response)
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<DefaultApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshRequest request){
        AuthResponse response = authService.refresh(request);
        DefaultApiResponse<AuthResponse> apiResponse = DefaultApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Tokens refreshed successfully")
                .data(response)
                .timestamp(System.currentTimeMillis())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok("Hello");
    }
}