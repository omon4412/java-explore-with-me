package ru.practicum.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.user.LoginRequest;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.service.AuthService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody @Valid LoginRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@RequestBody @Valid NewUserRequest registrationUserDto) {
        return new ResponseEntity<>(authService.createNewUser(registrationUserDto), HttpStatus.CREATED);
    }
}
