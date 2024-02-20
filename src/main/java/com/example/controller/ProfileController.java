package com.example.controller;

import com.example.dto.*;
import com.example.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name="Profile", description = "Profile api list.")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/updateAddress")
    @Operation(summary = "update profile address", description = "This api used for uploading profile address ...")
    ResponseEntity<ApiResponseDTO> updateAddress(@RequestBody ProfileAddressDTO dto){
        return ResponseEntity.ok(profileService.updateAddress(dto));
    }

    @PutMapping("/updateInfo")
    @Operation(summary = "update profile info", description = "This api used for updating profile info ...")
    ResponseEntity<ApiResponseDTO> updateInfo(@RequestBody UpdateProfileInfoDTO dto){
        System.out.println(dto);
        return ResponseEntity.ok(profileService.updateInfo(dto));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/updatePhoto")
    @Operation(summary = "update profile photo", description = "This api used for updating profile photo ...")
    ResponseEntity<ApiResponseDTO> updatePhoto(@RequestBody UpdateProfileAttach dto){
        return ResponseEntity.ok(profileService.updatePhoto(dto));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/sendVerificationCode")
    @Operation(summary = "sendVerificationCode", description = "This api used for sendVerificationCode ...")
    ResponseEntity<ApiResponseDTO> sendVerificationCode(@Valid @RequestBody SendVerificationCodeDTO dto){
        return ResponseEntity.ok(profileService.sendVerificationCode(dto));
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/verifyCode")
    @Operation(summary = "verifying code", description = "This api used for verifying sms code ...")
    ResponseEntity<ApiResponseDTO> verifyCode(@Valid @RequestBody ReceiveVerificationCodeDTO dto){
        return ResponseEntity.ok(profileService.verifyCode(dto));
    }
    @PreAuthorize("hasAnyRole('CUSTOMER','ROLE_ADMIN', 'ROLE_STAFF')")
    @PostMapping("/changePassword")
    @Operation(summary = "change password", description = "This api used for changing password ...")
    ResponseEntity<ApiResponseDTO> changePassword(@Valid @RequestBody ChangePasswordDTO dto){
        return ResponseEntity.ok(profileService.changePassword(dto));
    }
    @GetMapping("/public/verification/password/{jwt}")
    @Operation(summary = "verify email", description = "This api used for verifying email...")
    public ResponseEntity<String> verification(@PathVariable String jwt){
        return ResponseEntity.ok(profileService.passwordVerification(jwt));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/createStaff")
    @Operation(summary = "createStaff", description = "This api used for creating staff ...")
    ResponseEntity<ApiResponseDTO> createStaff(@Valid @RequestBody CreateStaffDTO dto){
        return ResponseEntity.ok(profileService.createStaff(dto));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateStaff")
    @Operation(summary = "updateStaff", description = "This api used for updateStaff  ...")
    ResponseEntity<ApiResponseDTO> updateStaff(@Valid @RequestBody CreateStaffDTO dto){
        return ResponseEntity.ok(profileService.updateStaff(dto));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getStaffList")
    @Operation(summary = "getStaffList", description = "This api used for getting StaffList  ...")
    ResponseEntity<List<ProfileDTO>> getStaffList(){
        return ResponseEntity.ok(profileService.getStaffList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteProfile")
    @Operation(summary = "deleteProfile", description = "This api used for deleteProfile  ...")
    ResponseEntity<ApiResponseDTO> deleteProfile(@RequestParam("email")String email){
        return ResponseEntity.ok(profileService.deleteProfile(email));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/resetStaff")
    @Operation(summary = "resetStaff", description = "This api used for resetStaff  ...")
    ResponseEntity<ApiResponseDTO> resetStaff(@RequestParam("email")String email){
        return ResponseEntity.ok(profileService.resetStaff(email));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/searchPagination")
    @Operation(summary = "searchPagination", description = "This api used for searchPagination  ...")

    public ResponseEntity<PageImpl<ProfileDTO>>filterPagination(@RequestBody FilterProfileDTO dto,
                                                                     @RequestParam(value = "page",defaultValue = "1") Integer page,
                                                                     @RequestParam(value = "size",defaultValue = "10") Integer size){
        return ResponseEntity.ok(profileService.filterPagination(page-1,size,dto));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateCustomer")
    @Operation(summary = "updateCustomer", description = "This api used for updateCustomer  ...")
    ResponseEntity<ApiResponseDTO> updateCustomer(@Valid @RequestBody UpdateCustomerDTO dto){
        return ResponseEntity.ok(profileService.updateCustomer(dto));
    }
}
