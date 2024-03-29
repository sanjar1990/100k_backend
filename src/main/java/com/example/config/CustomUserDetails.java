package com.example.config;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private ProfileEntity profile;
    public CustomUserDetails(ProfileEntity profile){
        this.profile=profile;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority>list=new LinkedList<>();
list.add(new SimpleGrantedAuthority(profile.getRole().name()));
        return list;
    }

    @Override
    public String getPassword() {
        return profile.getPassword();
    }

    @Override
    public String getUsername() {
        return profile.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return profile.getVisible();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return profile.getStatus().equals(ProfileStatus.ACTIVE);
    }

    public ProfileEntity getProfile(){
        return profile;
    }

    public Integer getProfileId(){
        return profile.getId();
    }
}
