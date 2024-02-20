package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateCustomerDTO {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String gender;
    private String attachUrl;
    private String role;
    private String address;
    private String status;
    private boolean visible;
}
