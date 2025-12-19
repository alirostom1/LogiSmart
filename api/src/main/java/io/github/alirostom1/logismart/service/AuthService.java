package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.LoginRequest;
import io.github.alirostom1.logismart.dto.request.RefreshRequest;
import io.github.alirostom1.logismart.dto.request.person.RegisterRequest;
import io.github.alirostom1.logismart.dto.response.common.AuthResponse;
import io.github.alirostom1.logismart.dto.response.common.TokenPair;
import io.github.alirostom1.logismart.exception.EmailAlreadyExistsException;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.PersonMapper;
import io.github.alirostom1.logismart.mapper.SenderMapper;
import io.github.alirostom1.logismart.model.entity.Role;
import io.github.alirostom1.logismart.model.entity.Sender;
import io.github.alirostom1.logismart.model.entity.User;
import io.github.alirostom1.logismart.model.enums.ERole;
import io.github.alirostom1.logismart.repository.RoleRepository;
import io.github.alirostom1.logismart.repository.SenderRepo;
import io.github.alirostom1.logismart.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.h2.command.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PersonMapper personMapper;
    private final SenderRepo senderRepo;
    private final RoleRepository roleRepository;
    private final SenderMapper senderMapper;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse("ROLE_RECIPIENT");
        TokenPair tokenPair = jwtService.generateTokenPair(user,null);
        return AuthResponse.builder()
                .personResponse(personMapper.toResponse(user))
                .tokenPair(tokenPair)
                .role(role)
                .build();
    }
    public AuthResponse register(RegisterRequest request){
        if(senderRepo.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        if(senderRepo.existsByPhone(request.getPhone())){
            throw new PhoneAlreadyExistsException(request.getPhone());
        }
        Sender sender = senderMapper.toSenderEntity(request);
        sender.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findByName(ERole.ROLE_SENDER)
                .orElseThrow(() -> new ResourceNotFoundException("Sender Role not found!"));
        sender.setRole(role);
        Sender savedSender = senderRepo.save(sender);
        TokenPair tokenPair = jwtService.generateTokenPair(sender,null);
        return AuthResponse.builder()
                .tokenPair(tokenPair)
                .role(savedSender.getRole().getName() + "")
                .personResponse(senderMapper.entitytoRegisterResponse(savedSender))
                .build();
    }
    public AuthResponse refresh(RefreshRequest request){
        User user = jwtService.extractUser(request.getRefreshToken());
        TokenPair tokenPair = jwtService.rotateRefreshToken(request.getRefreshToken(),user);

        return AuthResponse.builder()
                .personResponse(personMapper.toResponse(user))
                .tokenPair(tokenPair)
                .role(user.getRole() + "")
                .build();
    }
}
