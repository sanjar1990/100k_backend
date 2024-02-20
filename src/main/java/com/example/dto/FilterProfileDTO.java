package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterProfileDTO {
        private String name;
        private String email;
        private String phone;
        private String birthDateFrom;
        private String birthDateTo;
        private String gender;
        private String role;
        private String status;
        private boolean visible;
    }

