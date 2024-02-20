package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AuthDTO;
import com.example.dto.ProfileDTO;
import com.example.dto.RegistrationDTO;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="Auth", description = "Auth api list.")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    @Operation(summary = "registration", description = "This api used for registration ...")
    public ResponseEntity<ApiResponseDTO>registration(@Valid @RequestBody RegistrationDTO dto){
        System.out.println("registration request: "+dto.getAttachId());
        return ResponseEntity.ok(authService.registration(dto));

    }

    @GetMapping("/verification/email/{jwt}")
    @Operation(summary = "verify email", description = "This api used for verifying email...")
    public ResponseEntity<String> verification(@PathVariable String jwt){
        return ResponseEntity.ok(authService.emailVerification(jwt));
    }
    @PostMapping("/login")
    @Operation(summary = "login", description = "This api used for login...")
    public ResponseEntity<ProfileDTO>login(@Valid @RequestBody AuthDTO authDTO){
        return ResponseEntity.ok(authService.login(authDTO));
    }

}
