package com.stys.platform.pages;

import play.libs.F;

/**
 * Repository service. Pages may be stored in a repository 
 * with any backend. Clients may choose to implement repository
 * over sql database, or key-value storage, or file system, or
 * anything else.
 */
public interface Repository<T> {

	/**
	 * Store page in repository
	 * @param namespace - namespace of page
	 * @param key - name of page
	 * @param version - version of page
	 * @param page - contents of page
	 */
    public void put(String namespace, String key, F.Option<Long> version, T page);

    /**
     * Get page from repository
     * @param namespace - namespace of page
     * @param key - name of page
     * @param version - version of page
     * @return
     */
    public F.Option<T> get(String namespace, String key, F.Option<Long> version);

}
