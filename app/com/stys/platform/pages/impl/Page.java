package com.stys.platform.pages.impl;

public class Page {

	public String namespace;
	
	public String key;

	public String status = DRAFT;
	public static String DRAFT = "draft";
	public static String PUBLISHED = "published";
	public static String DELETED = "deleted";

	public String template;

	public Long revision;

	public String title;

	public String source;

	public String content;

	
}
