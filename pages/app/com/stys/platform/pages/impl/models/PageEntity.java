package com.stys.platform.pages.impl.models;

import java.util.List;

import javax.persistence.*;

import play.db.ebean.Model;

/**
 * Page root entity
 */
@Entity
@Table(name = "pages")
public class PageEntity extends Model {

	private static final long serialVersionUID = 1L;

	@Id
    public Long id;

    public String namespace;

    @Column(name = "key_")
    public String key;

    @OneToOne
    public AccessEntity access;
    
    @OneToOne
    public StateEntity state;

    @OneToOne
    public RevisionEntity revision;
    
    @OneToMany(mappedBy = "page")
    public List<RevisionEntity> revisions;
    
    @OneToOne
    public MetaEntity meta;

    public static final Finder<Long, PageEntity> find = new Finder<>(Long.class, PageEntity.class);

}