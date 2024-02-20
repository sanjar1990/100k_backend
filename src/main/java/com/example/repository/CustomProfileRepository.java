package com.example.repository;

import com.example.dto.FilterProfileDTO;
import com.example.dto.FilterResultDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.Gender;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomProfileRepository {
    @Autowired
    private EntityManager entityManager;

    public FilterResultDTO<ProfileEntity> filterPagination(Integer page, Integer size, FilterProfileDTO dto){
        StringBuilder selectBuilder=new StringBuilder(" from ProfileEntity as p");
        StringBuilder countBuilder=new StringBuilder("select count(id) from ProfileEntity as p");
        StringBuilder builder=new StringBuilder(" where 1=1");
        Map<String, Object>params=new LinkedHashMap<>();

        if(dto.getEmail()!=null){
        builder.append(" and p.email=:email");
        params.put("email",dto.getEmail());
        }
        if(dto.getName()!=null){
            builder.append(" and p.name=:name");
            params.put("name", dto.getName());
        }
        if(dto.getPhone()!=null){
            builder.append(" and p.phone=:phone");
            params.put("phone", dto.getPhone());
        }
        if(dto.getGender()!=null){
            builder.append(" and p.gender=:gender");
            params.put("gender", Gender.valueOf(dto.getGender().toUpperCase()));
        }
        if(dto.getRole()!=null){
            builder.append(" and p.role=:role");
            params.put("role", ProfileRole.valueOf(dto.getRole().toUpperCase()));
        }
        if(dto.getStatus()!=null){
            builder.append(" and p.status=:status");
            params.put("status", ProfileStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        if(dto.getBirthDateFrom()!=null){
            builder.append(" and p.birthDate>=:from");
            params.put("from", LocalDate.parse(dto.getBirthDateFrom()));
        }
        if(dto.getBirthDateTo()!=null){
            builder.append(" and p.birthDate<=:to");
            params.put("to", LocalDate.parse(dto.getBirthDateTo()));
        }
        if(dto.isVisible()){
            builder.append( " and p.visible=true");
        }
        if(!dto.isVisible()){
            builder.append(" and p.visible=false");
        }
        countBuilder.append(builder);
        builder.append( " order by p.createdDate desc");
        selectBuilder.append(builder);
        Query selectQuery=entityManager.createQuery(selectBuilder.toString());
        Query countQuery=entityManager.createQuery(countBuilder.toString());
        selectQuery.setFirstResult(page*size);
        selectQuery.setMaxResults(size);
        for (Map.Entry<String, Object> p: params.entrySet()){
            selectQuery.setParameter(p.getKey(), p.getValue());
            countQuery.setParameter(p.getKey(), p.getValue());
        }
        List<ProfileEntity>entityList=selectQuery.getResultList();
        Long totalElement=(Long)countQuery.getSingleResult();
        return  new FilterResultDTO<>(entityList, totalElement);
    }
}
