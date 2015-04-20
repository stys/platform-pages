package com.stys.platform.pages.impl.models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Publication states of pages
 */
@Entity
@Table(name="pages_state")
public class StateEntity extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    @Column(name = "state_")
    public String state;

    @ManyToOne
    public PageEntity page;

    @CreatedTimestamp
    public Timestamp createDateTime;
    
    public static final Model.Finder<Long, StateEntity> find = new Model.Finder<>(Long.class, StateEntity.class);


}
