package com.example.util;

import com.example.config.CustomUserDetails;
import com.example.entity.ProfileEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {
    public static  Integer getProfileId(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails=(CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getProfileId();
    }
    public  static ProfileEntity getProfileEntity(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal().equals("anonymousUser")) return null;
        CustomUserDetails customUserDetails= (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getProfile();
    }

}
