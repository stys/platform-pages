package com.stys.platform.pages.repository.models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/** Publication states of pages */
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

    @UpdatedTimestamp
    public Timestamp updateDateTime;
    
    public static final Model.Finder<Long, StateEntity> find = new Model.Finder<>(StateEntity.class);

}
