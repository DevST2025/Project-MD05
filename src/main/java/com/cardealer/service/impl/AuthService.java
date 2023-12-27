package com.cardealer.service.impl;

import com.cardealer.exception.LoginFailException;
import com.cardealer.model.dto.request.LoginRequest;
import com.cardealer.model.dto.response.AuthResponse;
import com.cardealer.model.entity.User;
import com.cardealer.repository.IUserRepository;
import com.cardealer.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;
    private final JwtProvider jwtProvider;
    public AuthResponse login(LoginRequest loginRequest) throws LoginFailException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException ex) {
            throw new LoginFailException("Username or password incorrect");
        }
        User user = userRepository.loadByUsername(loginRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        if (!user.getStatus()) {
            throw new LoginFailException("Tài khoản của bạn đã bị khoá, vui lòng liên hệ với Admin để được hỗ trợ");
        }
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        return AuthResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .fullName(user.getFullName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
