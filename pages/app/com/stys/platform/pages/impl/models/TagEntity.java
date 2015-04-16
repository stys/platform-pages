package com.stys.platform.pages.impl.models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Page tags
 */
@Entity
@Table(name = "pages_tags")
public class TagEntity extends Model {

    private static final long serialVersionUID = 1L;
    
    @Id
    public Long id;

    public String tag;
    
    public PageEntity page;
    
    public static final Finder<Long, TagEntity> find = new Finder<>(Long.class, TagEntity.class);
    
}
