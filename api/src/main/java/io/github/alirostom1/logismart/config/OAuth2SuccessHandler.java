package io.github.alirostom1.logismart.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.alirostom1.logismart.dto.response.common.AuthResponse;
import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
import io.github.alirostom1.logismart.dto.response.common.TokenPair;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.PersonMapper;
import io.github.alirostom1.logismart.model.entity.Role;
import io.github.alirostom1.logismart.model.entity.Sender;
import io.github.alirostom1.logismart.model.entity.User;
import io.github.alirostom1.logismart.model.enums.AuthProvider;
import io.github.alirostom1.logismart.model.enums.ERole;
import io.github.alirostom1.logismart.repository.RoleRepository;
import io.github.alirostom1.logismart.repository.SenderRepo;
import io.github.alirostom1.logismart.repository.UserRepo;
import io.github.alirostom1.logismart.service.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JWTService jwtService;
    private final SenderRepo senderRepo;
    private final UserRepo userRepo;
    private final RoleRepository roleRepo;
    private final PersonMapper personMapper;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());

        User user = getOrCreateUser(provider,authentication.getPrincipal());

        TokenPair tokenPair = jwtService.generateTokenPair(user,null);
        AuthResponse response1 = AuthResponse.builder()
                .tokenPair(tokenPair)
                .personResponse(personMapper.toResponse(user))
                .role(user.getRole().getName().name())
                .build();
        DefaultApiResponse<AuthResponse> apiResponse = DefaultApiResponse.<AuthResponse>builder()
                .success(true)
                .data(response1)
                .message("Authenticated successfully!")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);


        String jsonResponse = mapper.writeValueAsString(apiResponse);
        String encodedResponse = URLEncoder.encode(jsonResponse,"UTF-8");
        String redirectUrl = String.format("%s/auth/callback#response=%s",frontendUrl,encodedResponse);
        response.sendRedirect(redirectUrl);
    }

    private User getOrCreateUser(AuthProvider provider,Object principal){
        String providerId;
        String email;
        String firstName;
        String lastName;

        if(principal instanceof OidcUser){
            OidcUser oidcUser = (OidcUser) principal;
            providerId = oidcUser.getSubject();
            email = oidcUser.getEmail();
            String fullName = oidcUser.getFullName();
            int firstSpace = fullName.indexOf(" ");
            firstName = fullName.substring(0,firstSpace);
            lastName = fullName.substring(firstSpace + 1);
        }else{
            OAuth2User oAuth2User = (OAuth2User) principal;
            providerId = (String) oAuth2User.getAttribute("id");
            email = (String) oAuth2User.getAttribute("email");
            if (provider == AuthProvider.FACEBOOK) {
                String fullName = (String) oAuth2User.getAttribute("name");
                if (fullName != null && fullName.contains(" ")) {
                    int firstSpace = fullName.indexOf(" ");
                    firstName = fullName.substring(0, firstSpace);
                    lastName = fullName.substring(firstSpace + 1);
                } else {
                    firstName = fullName != null ? fullName : "USER";
                    lastName = "USER";
                }
            } else {
                firstName = (String) oAuth2User.getAttribute("first_name");
                lastName = (String) oAuth2User.getAttribute("last_name");
            }
        }

        User user;

        Optional<User> existingUser = userRepo.findByProviderAndProviderId(provider,providerId);

        if(existingUser.isEmpty()){
            Optional<User> userWithSameEmail = userRepo.findByEmail(email);
            if(userWithSameEmail.isEmpty()){
                Role role = roleRepo.findByName(ERole.ROLE_SENDER)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found!"));
                Sender newSender = Sender.builder()
                        .email(email)
                        .role(role)
                        .firstName(firstName)
                        .lastName(lastName)
                        .provider(provider)
                        .providerId(providerId)
                        .build();
                user = senderRepo.save(newSender);
            }else{
                user = userWithSameEmail.get();
                user.setProvider(provider);
                user.setProviderId(providerId);
                user = userRepo.save(user);
            }
        }else{
            user = existingUser.get();
        }
        return user;
    }

}
