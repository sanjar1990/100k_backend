package com.example.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileAddressDTO {
    @NotNull(message = "enter the address")
    private String address;
}
