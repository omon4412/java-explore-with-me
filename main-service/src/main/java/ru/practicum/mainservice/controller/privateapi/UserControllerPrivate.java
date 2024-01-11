package ru.practicum.mainservice.controller.privateapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.service.UserService;


@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
public class UserControllerPrivate {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.getUserInfo(userId), HttpStatus.OK);
    }
}
