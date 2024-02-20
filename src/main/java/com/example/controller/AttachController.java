package com.example.controller;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AttachDTO;
import com.example.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/attach")
@Tag(name="Attach", description = "Attach api list.")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/public/upload")
    @Operation(summary = "upload photo", description = "This api used for uploading attaches ...")
    public ResponseEntity<AttachDTO>upload(@RequestParam("file")MultipartFile file){
        return  ResponseEntity.ok(attachService.upload(file));
    }
    @GetMapping(value = "/public/{id}/img" ,produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(summary = "get photo", description = "This api used for getting attaches ...")
    public byte[]openByIdImage(@PathVariable String id){
    return attachService.openById(id);
    }
    @GetMapping("/public/download/{id}")
    @Operation(summary = "download photo", description = "This api used for downloading attaches ...")
    public ResponseEntity<Resource>download(@PathVariable String id){
    return attachService.download(id);
    }

    @DeleteMapping("/public/{id}")
    @Operation(summary = "delete photo", description = "This api used for deleting attaches ...")
    public ResponseEntity<ApiResponseDTO>delete(@PathVariable(name = "id")String id){
        return ResponseEntity.ok(attachService.delete(id));
    }



}
