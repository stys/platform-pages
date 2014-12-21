package com.stys.platform.pages.impl;

import java.sql.Timestamp;

import javax.persistence.*;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

/**
 * Simple implementation of database page storage.
 */
@Entity
public class PageEntity extends Model {

    /**
	 * Default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * A naive design would be to create a table with namespace, key and version columns,
	 * The version column would store a version number for an item, identified by namespace and key.
	 * A composite unique constrain would be imposed on a namespace + key + version.
	 * 
	 * In this design it would be impossible to consistently increment version numbers in a
	 * concurrent settings, because to increment a version we would first need to get
	 * a value of previous version by a separate request. This would create a time window
	 * in which another user may create a new record and increment a version of the same 
	 * item. This would result in a version collision and a violation of a unique constraint.
	 * 
	 * Thus a resonable way is to create a global autoincremented revision counter and make 
	 * it the primary key of the table. The composite unique constraint becomes unneccesary, because
	 * the primary key itself is unique.  
	 */
	
	/** 
	 * Global revision number. 
	 */ 
	@Id
    public Long revision;

    public String namespace;

    public String key;
    
    public String title;
    
    @Lob
    public String content;
    
    public String template;

    @CreatedTimestamp
    public Timestamp createDateTime;

    @UpdatedTimestamp
    public Timestamp updateDateTime;

    public static final Finder<Long, PageEntity> find =
            new Finder<Long, PageEntity>(Long.class, PageEntity.class);
    
    public static PageEntity create(String namespace, String key, String title, String content, String template) {
    	PageEntity page = new PageEntity();
    	page.namespace = namespace;
    	page.key = key;
    	page.title = title;
    	page.content = content;
    	page.template = template;
    	page.save();
    	return page;
    }
    

}
