package com.example.repository;

import com.example.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity,String> {
        Optional<CategoryEntity>findByNameAndOrderNumAndVisibleTrue(String name, Integer orderNum);
        List<CategoryEntity>findByVisibleTrueOrderByOrderNum();
        Optional<CategoryEntity> findByIdAndVisibleTrue(String id);
        @Transactional
        @Modifying
        @Query("update CategoryEntity set visible=false where id=:id")
        int deleteCategory(@Param("id")String id);
        @Transactional
        @Modifying
        @Query("update CategoryEntity set visible=true where id=:id")
        int resetCategory(@Param("id")String id);
}
