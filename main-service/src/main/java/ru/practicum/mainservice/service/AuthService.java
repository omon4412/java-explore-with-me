package ru.practicum.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.mainservice.dto.user.LoginRequest;
import ru.practicum.mainservice.dto.user.NewUserRequest;
import ru.practicum.mainservice.dto.user.UserDto;
import ru.practicum.mainservice.exception.BadRequestException;
import ru.practicum.mainservice.mapper.UserMapper;
import ru.practicum.mainservice.model.User;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody LoginRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.findByUsername(authRequest.getUsername()).get();
            return ResponseEntity.ok(UserMapper.toUserDto(user));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    public UserDto createNewUser(@RequestBody NewUserRequest registrationUserDto) {
        if (userService.findByUsername(registrationUserDto.getName()).isPresent()) {
            throw new BadRequestException("Пользователь с указанным именем уже существует");
        }
        User user = userService.createNewUser(registrationUserDto);
        return UserMapper.toUserDto(user);
    }
}
