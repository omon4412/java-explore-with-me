package ru.practicum.mainservice.dto.jwt;

import lombok.Data;

@Data
public class JwtRequest {
    private String name;
    private String password;
}
