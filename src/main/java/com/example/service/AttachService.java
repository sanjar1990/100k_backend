package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.exception.ItemNotFoundException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.UUID;

@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${attach.folder.name}")
    private String folderName;
    @Value("${attach.url}")
    private String attachUrl;
    // uploading photo
    public AttachDTO upload(MultipartFile file) {
        if(file==null||file.isEmpty() )throw new ItemNotFoundException("file not found");
        String pathFolder=getYMD();
        File folder=new File(folderName+"/"+pathFolder);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String key= UUID.randomUUID().toString();
        String extension=getExtension(file.getOriginalFilename());
        try {
        byte [] bytes= file.getBytes();
            Path path= Paths.get(folderName+"/"+pathFolder+"/"+key+"."+extension);
            Files.write(path,bytes);
            AttachEntity entity=new AttachEntity();
            entity.setId(key);
            entity.setSize(file.getSize());
            entity.setExtension(extension);
            entity.setOriginalName(file.getOriginalFilename());
            entity.setPath(pathFolder);
            System.out.println(pathFolder);
            attachRepository.save(entity);
            AttachDTO dto=new AttachDTO();
            dto.setUrl(getUrl(entity.getId()));
            dto.setId(entity.getId());
            System.out.println("key: "+key);
            System.out.println("dtoKey: "+dto.getId());
            System.out.println("entity id : "+entity.getId());

            return dto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //    2. Get Attach By Id (Open)
    public byte[] openById(String id) {
        AttachEntity entity=get(id);
    try {
        BufferedImage originalImage= ImageIO.read(new File(url(entity.getPath(),entity.getId(),entity.getExtension())));
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(originalImage,entity.getExtension(),baos);
        baos.flush();
        byte []imageInByte=baos.toByteArray();
        baos.close();
        return imageInByte;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    }

    private String getYMD(){
        int year= Calendar.getInstance().get(Calendar.YEAR);
        int month=Calendar.getInstance().get(Calendar.MONTH);
        int day=Calendar.getInstance().get(Calendar.DATE);
        return year+"/"+month+"/"+day;
    }
    private String getExtension(String fileName){
        int lastIndex=fileName.lastIndexOf(".");
        return fileName.substring(lastIndex+1);
    }
    public String getUrl(String id){
        return attachUrl+"/public/"+id+"/img";
    }
    public AttachEntity get(String id){
        return attachRepository.findById(id).orElseThrow(()->new ItemNotFoundException("attach not found"));
    }
    private String url(String path, String id, String extension){
        return folderName+"/"+path+"/"+id+"."+extension;
    }

    public ResponseEntity<Resource> download(String id) {
        AttachEntity entity = get(id);
        try {
            Path file = Paths.get(url(entity.getPath(), entity.getId(), entity.getExtension()));
            org.springframework.core.io.Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public ApiResponseDTO delete(String id) {
    boolean t=false;
    AttachEntity entity=get(id);
    File file=new File(url(entity.getPath(),entity.getId(),entity.getExtension()));
    if(file.exists()){
        t= file.delete();
    }
    attachRepository.deleteById(id);
    return t? new ApiResponseDTO(false,"deleted"): new ApiResponseDTO(false,"not deleted");

    }
}
