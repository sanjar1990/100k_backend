package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    @Email(message = "invalid email format")
    private String email;
    @NotNull(message = "enter the password")
    private String password;
}
