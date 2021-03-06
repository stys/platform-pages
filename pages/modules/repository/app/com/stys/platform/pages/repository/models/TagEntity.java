package com.stys.platform.pages.repository.models;

import com.avaje.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "pages_tags")
public class TagEntity extends Model {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;

    public String tag;
    
    public PageEntity page;
    
    public static final Finder<Long, TagEntity> find = new Finder<>(TagEntity.class);
    
}
