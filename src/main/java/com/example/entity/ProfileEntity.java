package com.example.entity;

import com.example.enums.Gender;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseIntEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "birthday")
    private LocalDate birthDate;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ProfileRole role;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;
    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id",updatable = false,insertable = false)
    private AttachEntity attach;


}
