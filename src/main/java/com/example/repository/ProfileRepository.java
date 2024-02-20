package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer> {


    Optional<ProfileEntity>findByEmailAndVisibleTrue(String email);
    Optional<ProfileEntity>findByEmailAndPasswordAndVisibleTrue(String email, String password);
    Optional<ProfileEntity>findByIdAndStatusAndVisibleTrue(Integer id, ProfileStatus status);
   boolean existsByEmailOrPhoneAndVisibleTrue(String email, String phone);
   @Query("from ProfileEntity where role in(:role) and visible=true order by createdDate desc")
   List<ProfileEntity>findByRoleAndVisibleTrue(List<ProfileRole> role);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible=false, status=NOT_ACTIVE where email=:email")
    int deleteStaff(@Param("email")String email);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible=true, status=ACTIVE where email=:email")
    int resetStaff(@Param("email") String email);
}
