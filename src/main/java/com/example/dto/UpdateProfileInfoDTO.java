package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateProfileInfoDTO {
    private String name;
    private String phone;
    private String birthDate;
    private String gender;
}
