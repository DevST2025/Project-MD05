package com.cardealer.controller;

import com.cardealer.exception.LoginFailException;
import com.cardealer.model.dto.request.LoginRequest;
import com.cardealer.model.dto.request.RegisterRequest;
import com.cardealer.model.dto.response.AuthResponse;
import com.cardealer.service.IUserService;
import com.cardealer.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.cardealer.com/v1/auth")
public class AuthController {
    private final IUserService userService;
    private final AuthService authService;
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return new ResponseEntity<>("Register is success", HttpStatus.CREATED);
    }
    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody LoginRequest request) throws LoginFailException {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

}
