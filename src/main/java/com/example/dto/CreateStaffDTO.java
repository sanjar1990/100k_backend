package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStaffDTO {
    @NotNull(message = "name is required")
    @Size(min = 3, message = "name character should be more then 3")
    private String name;
    @Email(message = "enter valid email")
    private String email;
    @NotNull(message = "phone is required")
    private String phone;
    @NotNull(message = "password is required")
    @Size(min = 6, message = "password character should be more then 6")
    private String password;
    @NotNull(message = "birthDate is required")
    private String birthDate;
    private String attachUrl;
}
