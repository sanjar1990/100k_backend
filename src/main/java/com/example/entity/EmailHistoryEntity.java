package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "email_history")
@Getter
@Setter
public class EmailHistoryEntity extends BaseStringEntity {
    @Column(name = "to_email")
    private String toEmail;
    @Column(name = "title", columnDefinition = "TEXT")
    private String title;
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    @Column(name = "user_id")
    private Integer userId;

}
