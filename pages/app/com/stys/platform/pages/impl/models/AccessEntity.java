package com.stys.platform.pages.impl.models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Pages access settings
 */
@Entity
@Table(name="pages_access")
public class AccessEntity extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @Column(name = "access_")
    public String access;

    @ManyToOne
    public PageEntity page;
    
    @CreatedTimestamp
    public Timestamp createDateTime;
    
    public static final Finder<Long, AccessEntity> find = new Finder<>(Long.class, AccessEntity.class);
    
}
