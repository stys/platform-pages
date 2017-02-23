package com.stys.platform.pages.repository.models;

import java.sql.Timestamp;
import javax.persistence.*;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

/** Page revision */
@Entity
@Table(name = "pages_revisions")
public class RevisionEntity extends Model {

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
	 * Thus a reasonable way is to create a global auto-incrementing revision counter and make
	 * it the primary key of the table. The composite unique constraint becomes unnecessary, because
	 * the primary key itself is unique.  
	 */
	
	/** Global revision number */
	@Id
    public Long id;

    @ManyToOne
    public PageEntity page;

    @Lob
    public String source;

    @Lob
    public String content;

    @CreatedTimestamp
    public Timestamp createDateTime;

    public static final Finder<Long, RevisionEntity> find = new Finder<>(RevisionEntity.class);

}
