package com.example.service;

import com.example.entity.EmailHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.repository.EmailHistoryRepository;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    public void sendEmail(ProfileEntity profile, String subject, String text) {
        EmailHistoryEntity entity= new EmailHistoryEntity();
        entity.setToEmail(profile.getEmail());
        entity.setMessage(text);
        entity.setTitle(subject);
        emailHistoryRepository.save(entity);
    }
    public void sendEmail(String email, String message, String subject) {
        EmailHistoryEntity entity= new EmailHistoryEntity();
        entity.setToEmail(email);
        entity.setMessage(message);
        entity.setTitle(subject);
        entity.setUserId(SpringSecurityUtil.getProfileId());
        emailHistoryRepository.save(entity);
    }
}
