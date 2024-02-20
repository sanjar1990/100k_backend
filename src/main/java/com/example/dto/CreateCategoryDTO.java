package com.example.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryDTO {
    @NotNull(message = "name is mandatory")
    private String name;
    @NotNull(message = "enter attach id")
    private String attachId;
    @NotNull(message = "enter order number")
    private Integer orderNum;
    private String parentId;
}
