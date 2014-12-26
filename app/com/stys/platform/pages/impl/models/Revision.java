package com.stys.platform.pages.impl.models;

import java.sql.Timestamp;

import javax.persistence.*;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

/**
 * Simple implementation of database page storage.
 */
@Entity
public class Revision extends Model {

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
	 * Thus a reasonable way is to create a global autoincrementing revision counter and make
	 * it the primary key of the table. The composite unique constraint becomes unnecessary, because
	 * the primary key itself is unique.  
	 */
	
	/** 
	 * Global revision number. 
	 */ 
	@Id
    public Long revision;

    @ManyToOne
    public Page page;

    public String title;

    @Lob
    public String source;

    @Lob
    public String content;

    @CreatedTimestamp
    public Timestamp createDateTime;

    @UpdatedTimestamp
    public Timestamp updateDateTime;

    public static final Finder<Long, Revision> find =
            new Finder<Long, Revision>(Long.class, Revision.class);
    
    public static Revision create(Page page, String title, String source, String content) {
    	Revision revision = new Revision();
    	revision.page = page;
        revision.title = title;
        revision.source = source;
    	revision.content = content;
    	revision.save();
    	return revision;
    }

}
