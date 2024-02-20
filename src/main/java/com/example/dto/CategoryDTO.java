package com.example.dto;

import com.example.entity.AttachEntity;
import com.example.entity.ProfileEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDTO {
    private String id;
    private String parentName;
    private String name;
    private Integer orderNum;
    private String creatorEmail;
    private String attachUrl;
    private Integer productCount;
}
