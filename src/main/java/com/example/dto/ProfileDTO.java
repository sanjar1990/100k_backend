package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String birthDate;
    private String gender;
    private String attachUrl;
    private String role;
    private String address;
    private String jwt;
    private String status;
    private boolean visible;

    public ProfileDTO(Integer id, String name, String email, String phone, String birthDate,
                      String gender, String attachUrl, String role, String address, String status,
                      boolean visible) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.attachUrl = attachUrl;
        this.role = role;
        this.address = address;
        this.status=status;
        this.visible=visible;
    }
}
