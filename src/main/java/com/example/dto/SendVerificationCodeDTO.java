package com.example.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendVerificationCodeDTO {
    @Email(message = "enter valid email")
    private String email;
}
