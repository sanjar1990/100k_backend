package com.example.service;

import com.example.dto.*;
import com.example.entity.ProfileEntity;
import com.example.enums.Gender;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Value("${attach.default.photo}")
    private String defaultPhoto;
    @Autowired
    private AttachService attachService;

    public ApiResponseDTO registration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional=profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        if(optional.isPresent()) {
            ProfileEntity entity = optional.get();
            if (!entity.getStatus().equals(ProfileStatus.REGISTRATION)) {
                return new ApiResponseDTO(true, "email already exists");
            } else {
                profileRepository.deleteById(entity.getId());
            }
        }
        ProfileEntity entity=new ProfileEntity();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        entity.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setRole(ProfileRole.ROLE_CUSTOMER);
        entity.setAddress("");
        if(dto.getAttachId()==null || dto.getAttachId().isBlank()){
            entity.setAttachId(defaultPhoto);
        }else {
            entity.setAttachId(dto.getAttachId());
        }
        System.out.println("AttachId "+dto.getAttachId());
        profileRepository.save(entity);
        mailSenderService.sendEmailVerification(entity);
        return new ApiResponseDTO(false,"The verification link was send to your email");

    }

    public String emailVerification(String jwt) {
        JwtDTO jwtDTO= JWTUtil.decode(jwt);
    Optional<ProfileEntity>optional=profileRepository.findByEmailAndVisibleTrue(jwtDTO.getEmail());
    if(optional.isEmpty()){
        throw new ItemNotFoundException("profile not found");
    }else {
        ProfileEntity entity=optional.get();
            if(!entity.getStatus().equals(ProfileStatus.REGISTRATION)){
                throw new AppBadRequestException("verification link is expired!");
            }
            entity.setStatus(ProfileStatus.ACTIVE);
            profileRepository.save(entity);
            return "You have been successfully verified";
    }
    }


    public ProfileDTO login(AuthDTO authDTO) {
        Optional<ProfileEntity>optional=profileRepository
                .findByEmailAndPasswordAndVisibleTrue(authDTO.getEmail(),MD5Util.encode(authDTO.getPassword()));
        if(optional.isEmpty())throw new ItemNotFoundException("Check your email or password");
        ProfileEntity entity=optional.get();
        if(!entity.getStatus().equals(ProfileStatus.ACTIVE)){
            throw new AppBadRequestException("your status is not active");
        }
        ProfileDTO dto= new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setGender(entity.getGender().name());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setBirthDate(entity.getBirthDate().toString());
        dto.setAttachUrl(attachService.getUrl(entity.getAttachId()));
        dto.setRole(entity.getRole().name());
        dto.setAddress(entity.getAddress());
        dto.setJwt(JWTUtil.encode(entity.getId(),entity.getEmail(),entity.getRole().name()));
        System.out.println("jwt: "+  dto.getJwt());
        log.info("login: "+authDTO.getEmail());
    return  dto;
    }

}
