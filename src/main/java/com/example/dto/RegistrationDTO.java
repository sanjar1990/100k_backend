package com.example.dto;

import com.example.enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class RegistrationDTO {
    @NotNull(message = "name is required")
    @Size(min = 3, message = "name character should be more then 3")
    private String name;
    @Email(message = "enter valid email")
    private String email;
    @NotNull(message = "phone is required")
//    @Size(min = 3, message = "phone character should be more then 9")
    private String phone;
    @NotNull(message = "password is required")
    @Size(min = 6, message = "password character should be more then 6")
    private String password;
    @NotNull(message = "birthDate is required")
    private String birthDate;
    @NotNull(message = "gender is required")
    private String gender;
    private String attachId;

}
