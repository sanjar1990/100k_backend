package com.example.repository;

import com.example.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, String> {
        Optional<EmailHistoryEntity> findByToEmailAndMessage(String toEmail, String code);
        @Query("from EmailHistoryEntity where toEmail=:toEmail and userId=:profileId order by createdDate desc limit 1")
        Optional<EmailHistoryEntity> findByToEmailAndProfileId(@Param("toEmail") String email, @Param("profileId") Integer profileId);
}
