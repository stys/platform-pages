package com.stys.platform.pages.impl.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.ebean.Model;

/**
 * Page meta information
 */
@Entity
@Table(name = "pages_meta")
public class MetaEntity extends Model{

	private static final long serialVersionUID = 1L;

	@Id
    public Long id;
	
	public String summary;
	
	public String thumb;
	
	public String description;
	
	public String keywords;
    
    public String category;
	
	@OneToOne
	public PageEntity page;
	
	public static final Finder<Long, MetaEntity> find = new Finder<>(Long.class, MetaEntity.class);

}
