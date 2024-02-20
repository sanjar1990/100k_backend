package com.example.dto;

import com.example.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtDTO {
    private Integer id;
    private String email;
    private ProfileRole role;
    private String newPassword;

    public JwtDTO(Integer id, String email, ProfileRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public JwtDTO(Integer id, String email, String newPassword) {
        this.id = id;
        this.email = email;
        this.newPassword = newPassword;
    }
}

