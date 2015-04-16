package com.stys.platform.pages.impl.models;

import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

public class PageMetaInfo extends Model{
	/**
	 * Default serial version id
	 */
	private static final long serialVersionUID = 1L;

	@Id
    public Long id;
	
	public String summary;
	
	public String thumbImage;
	
	public String metaDescription;
	
	public String metaKeyWords;
	
	@OneToOne
	public Page page;
	
	public static final Finder<Long, PageMetaInfo> find = new Finder<>(Long.class, PageMetaInfo.class);

	public static PageMetaInfo create(Page page, String summary, String thumbImage, String metaDescription, String metaKeyWords) {
		PageMetaInfo metaInfo = new PageMetaInfo();
		metaInfo.page = page;
		metaInfo.summary = summary;
		metaInfo.thumbImage = thumbImage;
		metaInfo.metaDescription = metaDescription;
		metaInfo.metaKeyWords = metaKeyWords;
		metaInfo.save();
		
		return metaInfo;
	}
}
