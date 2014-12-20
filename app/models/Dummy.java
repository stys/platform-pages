package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Dummy extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	public Long id;
	
	public String txt;
	
	public static final Finder<Long, Dummy> find = 
		new Finder<>(Long.class, Dummy.class);
	
}
