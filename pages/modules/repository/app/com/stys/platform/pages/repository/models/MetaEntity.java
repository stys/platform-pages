package com.stys.platform.pages.repository.models;

import javax.persistence.*;

import com.avaje.ebean.Model;

import java.sql.Timestamp;

/** Page meta information */
@Entity
@Table(name = "pages_meta")
public class MetaEntity extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;
	
    public String title;
    
    @Lob
	public String summary;
	
	public String thumb;
	        
    @Lob
	public String description;
	
    @Lob
	public String keywords;
    
    public String category;

    public String template;
	
	@OneToOne
	public PageEntity page;
	
	public static final Finder<Long, MetaEntity> find = new Finder<>(MetaEntity.class);

}
