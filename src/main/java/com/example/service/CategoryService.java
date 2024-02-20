package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CategoryDTO;
import com.example.dto.CreateCategoryDTO;
import com.example.entity.CategoryEntity;
import com.example.entity.ProfileEntity;
import com.example.exception.AppBadRequestException;
import com.example.repository.CategoryRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ProfileService profileService;

    public ApiResponseDTO createCategory(CreateCategoryDTO dto) {

        Optional<CategoryEntity> optional=categoryRepository.findByNameAndOrderNumAndVisibleTrue(dto.getName(), dto.getOrderNum());
        if(optional.isPresent()) throw new AppBadRequestException("Name or order number should be unique");
        CategoryEntity entity= new CategoryEntity();
        entity.setName(dto.getName());
        entity.setPrtId(SpringSecurityUtil.getProfileId());
        entity.setOrderNum(dto.getOrderNum());
        entity.setAttachId(dto.getAttachId());
        if(dto.getParentId()!=null){
           CategoryEntity parent=findById(dto.getParentId());
            entity.setParentId(parent.getId());
        }

        categoryRepository.save(entity);
        return new ApiResponseDTO(false, toDto(entity));
    }
    private CategoryEntity findById(String id){
        return categoryRepository.findByIdAndVisibleTrue(id).orElseThrow( ()->new AppBadRequestException("Category not found"));
    }

    public List<CategoryDTO> getCategory() {

        List<CategoryDTO> dtoList= categoryRepository.findByVisibleTrueOrderByOrderNum().stream()
                .map(this::toDto)
                .toList();
        System.out.println(dtoList);
        return  dtoList;
    }
    private CategoryDTO toDto(CategoryEntity entity){
        String parentName="";
        if(entity.getParentId() !=null){
            CategoryEntity category=findById(entity.getParentId());
            parentName=category.getName();
        }
        ProfileEntity profile=profileService.getProfileById(entity.getPrtId());
       return new CategoryDTO(
                entity.getId(),parentName,
                entity.getName(),entity.getOrderNum(),
               profile.getEmail(), attachService.getUrl(entity.getAttachId()),0);
    }

    public ApiResponseDTO deleteCategory(String id) {
        int result=categoryRepository.deleteCategory(id);
    return  result>0 ?new ApiResponseDTO(false,"Deleted"):
            new ApiResponseDTO(true,"Not deleted");
    }

    public ApiResponseDTO resetCategory(String id) {
        int result=categoryRepository.resetCategory(id);
        return  result>0?new ApiResponseDTO(false,"category recovered")
                :new ApiResponseDTO(true,"Category recover is failed");
    }

    public ApiResponseDTO updateCategory(String id, CreateCategoryDTO dto) {
        CategoryEntity entity=findById(id);
        entity.setName(dto.getName());
        entity.setOrderNum(dto.getOrderNum());
        entity.setParentId(dto.getParentId());
        if(!dto.getAttachId().isEmpty()){
            entity.setAttachId(dto.getAttachId());
        }
        categoryRepository.save(entity);
        return new ApiResponseDTO(false,"Category is updated");
    }
}
