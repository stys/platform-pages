package com.stys.platform.pages;

import play.libs.F;
import play.mvc.Content;

public interface Service<T> {

	/**
	 * Get page by namespace, key and optional revision
	 * @param namespace - namespace of requested page
	 * @param key - key of requested page
	 * @param revision - optional revision id of requested page
	 * @return - page rendered with template service
	 */
	public F.Option<Content> get(String namespace, String key,
			F.Option<Long> revision);

	/**
	 * Put page by namespace, key and optional revision
	 * @param page
	 * @param namespace
	 * @param key
	 * @param revision
	 * @return
	 */
	public void put(T page, String namespace, String key,
			F.Option<Long> revision);

}