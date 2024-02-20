package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Category")
public class CategoryEntity extends BaseStringEntity {
    @Column(name = "name", nullable = false)
    private  String name;
    @Column(name = "order_num", unique = true)
    private Integer orderNum;
    @Column(name = "prt_id")
    private Integer prtId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prt_id",updatable = false,insertable = false)
    private ProfileEntity profile;
    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id",updatable = false,insertable = false)
    private AttachEntity attach;
    @Column(name = "parent_id")
    private String parentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", updatable = false, insertable = false)
    private CategoryEntity category;

}
