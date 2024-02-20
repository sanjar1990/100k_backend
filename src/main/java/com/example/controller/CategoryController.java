package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CategoryDTO;
import com.example.dto.CreateCategoryDTO;
import com.example.dto.ProfileAddressDTO;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createCategory")
    @Operation(summary = "createCategory", description = "This api used for createCategory ...")
    ResponseEntity<ApiResponseDTO> createCategory(@RequestBody CreateCategoryDTO dto){
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/getCategory")
    @Operation(summary = "getCategory", description = "This api used for getCategory ...")
    ResponseEntity<List<CategoryDTO>> getCategory(){
        return ResponseEntity.ok(categoryService.getCategory());
    }
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @DeleteMapping("/deleteCategory/{id}")
    @Operation(summary = "deleteCategory", description = "This api used for deleteCategory ...")
    ResponseEntity<ApiResponseDTO> deleteCategory(@PathVariable String id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PutMapping("/resetCategory/{id}")
    @Operation(summary = "resetCategory", description = "This api used for resetCategory ...")
    ResponseEntity<ApiResponseDTO> resetCategory(@PathVariable String id){
        return ResponseEntity.ok(categoryService.resetCategory(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PutMapping("/updateCategory/{id}")
    @Operation(summary = "updateCategory", description = "This api used for updateCategory ...")
    ResponseEntity<ApiResponseDTO> updateCategory(@PathVariable String id,
                                                  @RequestBody CreateCategoryDTO dto){
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }
}
