package com.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {
    @NotNull(message = "password is required")
    @Size(min = 6, message = "password character should be more then 6")
    private String oldPassword;
    @NotNull(message = "password is required")
    @Size(min = 6, message = "password character should be more then 6")
    private String newPassword;
}
