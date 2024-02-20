package com.example.service;

import com.example.dto.*;
import com.example.entity.EmailHistoryEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.Gender;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exception.AppBadRequestException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.CustomProfileRepository;
import com.example.repository.EmailHistoryRepository;
import com.example.repository.ProfileRepository;
import com.example.util.JWTUtil;
import com.example.util.MD5Util;
import com.example.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private CustomProfileRepository customProfileRepository;
    @Value("${attach.default.photo}")
    private String defaultPhoto;

    public ApiResponseDTO updateAddress(ProfileAddressDTO dto) {
        Integer id= SpringSecurityUtil.getProfileId();
        Optional<ProfileEntity> optional=profileRepository.findById(id);
        if(optional.isEmpty()) throw new ItemNotFoundException("profile not found");
        ProfileEntity entity=optional.get();
        if(!entity.getVisible() || !entity.getStatus().equals(ProfileStatus.ACTIVE))
            throw new ItemNotFoundException("Your account is blocked");
        entity.setAddress(dto.getAddress());
        profileRepository.save(entity);
    return new ApiResponseDTO(false,"Address is updated");
    }

    public ApiResponseDTO updateInfo(UpdateProfileInfoDTO dto) {
        ProfileEntity entity=getProfile();
        entity.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        profileRepository.save(entity);
        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setName(dto.getName());
        profileDTO.setPhone(dto.getPhone());
        profileDTO.setBirthDate(dto.getBirthDate());
        profileDTO.setGender(dto.getGender().toLowerCase());
        return new ApiResponseDTO(false,profileDTO);
    }

    public ApiResponseDTO updatePhoto(UpdateProfileAttach dto) {
        ProfileEntity entity=getProfile();
        entity.setAttachId(dto.getAttachId());
        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setAttachUrl(attachService.getUrl(dto.getAttachId()));
        profileRepository.save(entity);
        return new ApiResponseDTO(false,profileDTO);
    }



    public ApiResponseDTO sendVerificationCode(SendVerificationCodeDTO dto) {
        Optional<EmailHistoryEntity> optional= emailHistoryRepository
                .findByToEmailAndProfileId(dto.getEmail(),SpringSecurityUtil.getProfileId());
        if(optional.isPresent()){
            EmailHistoryEntity entity=optional.get();
            if(LocalDateTime.now().minusMinutes(5).isBefore(entity.getCreatedDate())){
                return  new ApiResponseDTO(true,"You may send one email in every 5 minutes");
            }
        }

        mailSenderService.sendVerificationCode(dto.getEmail());
        return new ApiResponseDTO(false,"verification code was sent to yur email");
    }

    public ApiResponseDTO verifyCode(ReceiveVerificationCodeDTO dto) {
        Optional<EmailHistoryEntity> optional=emailHistoryRepository
                .findByToEmailAndMessage(dto.getEmail(), dto.getCode());
        ProfileEntity profileEntity=getProfile();
        if(profileEntity.getEmail().equals(dto.getEmail())){
            throw new AppBadRequestException("Your email is already updated");
        }
        if(optional.isEmpty()) throw new AppBadRequestException("Verification code is not valid");
        EmailHistoryEntity emailHistoryEntity= optional.get();
        if(!emailHistoryEntity.getMessage().equals(dto.getCode())){
            throw new AppBadRequestException("Verification code is not valid");
        }
        if(emailHistoryEntity.getCreatedDate().plusMinutes(5).isBefore(LocalDateTime.now())){
            throw new AppBadRequestException("Your verification code is expired");
        }

        profileEntity.setEmail(dto.getEmail());
        profileRepository.save(profileEntity);
        return new ApiResponseDTO(false, "Email is verified");

    }
    public ApiResponseDTO changePassword(ChangePasswordDTO dto) {
        ProfileEntity profileEntity=getProfile();
        if(!profileEntity.getPassword().equals(MD5Util.encode(dto.getOldPassword()))){
            throw new AppBadRequestException("Enter valid password");
        }
        mailSenderService.sendPasswordVerification(profileEntity, MD5Util.encode(dto.getNewPassword()));
        return new ApiResponseDTO(false,"Verification link sent to your email");
    }
    public String passwordVerification(String jwt) {
        JwtDTO jwtDTO= JWTUtil.decodeForPasswordVerification(jwt);
        Optional<ProfileEntity>optional=profileRepository.findByEmailAndVisibleTrue(jwtDTO.getEmail());
        if(optional.isEmpty()){
            throw new ItemNotFoundException("profile not found");
        }else {
            ProfileEntity entity=optional.get();
            if(!entity.getStatus().equals(ProfileStatus.ACTIVE)){
                throw new AppBadRequestException("Your account is blocked");
            }
            entity.setPassword(jwtDTO.getNewPassword());
            profileRepository.save(entity);
            return "You have been successfully verified";
        }
    }

    public ApiResponseDTO createStaff(CreateStaffDTO dto) {
        boolean isExists=profileRepository.existsByEmailOrPhoneAndVisibleTrue(dto.getEmail(), dto.getPhone());
        if(isExists) throw new AppBadRequestException("Email or password is exists");
        ProfileEntity entity= new ProfileEntity();
        entity.setName(dto.getName());
        entity.setRole(ProfileRole.ROLE_STAFF);
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setAddress("");
        entity.setGender(Gender.MALE);
        if(dto.getAttachUrl().trim().isEmpty()){
            entity.setAttachId(defaultPhoto);
        }else {
            entity.setAttachId(dto.getAttachUrl());
        }
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        profileRepository.save(entity);

        return  new ApiResponseDTO(false,"Staff is created");
    }
    private ProfileEntity getProfile(){
        Optional<ProfileEntity> optional=profileRepository
                .findByIdAndStatusAndVisibleTrue(SpringSecurityUtil.getProfileId(),ProfileStatus.ACTIVE);
        if(optional.isEmpty()) throw new ItemNotFoundException("Profile not found");
        return optional.get();
    }
    public ProfileEntity getProfileById(Integer id){
        Optional<ProfileEntity> optional=profileRepository
                .findByIdAndStatusAndVisibleTrue(id,ProfileStatus.ACTIVE);
        if(optional.isEmpty()) throw new ItemNotFoundException("Profile not found");
        return optional.get();
    }


    public List<ProfileDTO> getStaffList() {
        List<ProfileRole> roleList= new LinkedList<>();
        roleList.add(ProfileRole.ROLE_STAFF);
        roleList.add(ProfileRole.ROLE_ADMIN);
        return profileRepository.findByRoleAndVisibleTrue(roleList).stream()
                .filter(v->v.getStatus().equals(ProfileStatus.ACTIVE))
                .map( v -> {
                    ProfileDTO dto=  new ProfileDTO();
                dto.setRole(v.getRole().name());
                dto.setName(v.getName());
                dto.setEmail(v.getEmail());
                dto.setPhone(v.getPhone());
                dto.setBirthDate(v.getBirthDate().toString());
                dto.setAttachUrl(attachService.getUrl(v.getAttachId()));
                    return  dto;
                }).toList();
    }

    public ApiResponseDTO deleteProfile(String email) {
        int result=profileRepository.deleteStaff(email);
        return result>0 ?new ApiResponseDTO(false, "Staff is deleted"):
                new ApiResponseDTO(true,"Staff is not deleted");
    }

    public ApiResponseDTO resetStaff(String email) {
       int result= profileRepository.resetStaff(email);
    return  result>0?new ApiResponseDTO(false,"Staff is recovered")
            :new ApiResponseDTO(true,"Staff is not recovered");
    }

    public ApiResponseDTO updateStaff(CreateStaffDTO dto) {
       Optional<ProfileEntity> optional= profileRepository.findByEmailAndVisibleTrue(dto.getEmail());
        ProfileEntity profileEntity;
    if(optional.isEmpty()){
       profileEntity= new ProfileEntity();
       
        if(dto.getAttachUrl().isEmpty()){
            profileEntity.setAttachId(defaultPhoto);
        }else {
            profileEntity.setAttachId(dto.getAttachUrl());
        }
    }else {
        profileEntity=optional.get();

    }
        profileEntity.setBirthDate(LocalDate.parse(dto.getBirthDate()));
        profileEntity.setName(dto.getName());
        profileEntity.setEmail(dto.getEmail());
        profileEntity.setRole(ProfileRole.ROLE_STAFF);
        profileEntity.setPhone(dto.getPhone());
        profileEntity.setPassword(MD5Util.encode(dto.getPassword()));
        if(!dto.getAttachUrl().isEmpty()){
            profileEntity.setAttachId(dto.getAttachUrl());
        }
    profileRepository.save(profileEntity);
    return new ApiResponseDTO(false,"Staff is updated");
    }

    public PageImpl<ProfileDTO> filterPagination(Integer page, Integer size, FilterProfileDTO dto) {
        FilterResultDTO<ProfileEntity>filterResultDTO=customProfileRepository.filterPagination(page,size, dto);
        Pageable pageable= PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDate"));
        List<ProfileDTO> dtoList=filterResultDTO.getContent().stream().map(s-> {
           String gender="";
            if(s.getGender()!=null){
               gender=s.getGender().name();
            }
          return   new ProfileDTO(
                    s.getId(), s.getName(), s.getEmail(), s.getPhone(), s.getBirthDate().toString(),
                  gender, attachService.getUrl(s.getAttachId()), s.getRole().toString(), s.getAddress(),
                  s.getStatus().name(), s.getVisible()
            );
        }).toList();
        return  new PageImpl<>(dtoList,pageable,filterResultDTO.getTotalElement());
    }

    public ApiResponseDTO updateCustomer(UpdateCustomerDTO dto) {
        Optional<ProfileEntity> optional=profileRepository.findById(dto.getId());
        if(optional.isEmpty()) throw new ItemNotFoundException("Profile not found");
        ProfileEntity entity= optional.get();
        if(!dto.getAttachUrl().isEmpty()){
            entity.setAttachId(dto.getAttachUrl());
        }

        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setStatus(ProfileStatus.valueOf(dto.getStatus().toUpperCase()));
        if(!dto.getGender().isEmpty()){
            entity.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        }
        entity.setRole(ProfileRole.valueOf(dto.getRole().toUpperCase()));
        entity.setBirthDate(dto.getBirthDate());
        entity.setVisible(dto.isVisible());
        profileRepository.save(entity);
        return new ApiResponseDTO(false,"Updated");

    }
}
