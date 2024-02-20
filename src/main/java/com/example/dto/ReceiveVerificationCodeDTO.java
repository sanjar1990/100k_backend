package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class ReceiveVerificationCodeDTO {
    @Email(message = "enter valid email")
    private String email;
    @NotNull(message = "enter your password")
    private String password;
    @NotNull(message = "enter your code")
    private String code;

}
