package com.stys.platform.pages;

import play.libs.F;
import play.mvc.Content;

/**
 * Pages service. Implementors may consider to use Decorator pattern
 * to add aspects, such as access restrictions. 
 */
public abstract class Pages<T> {

	/*
	 * Injected template service
	 */
    private Template<T> template;
    
    /*
     * Injected repository service
     */
    private Repository<T> repository;

    /**
     * Constructor injection of services
     * @param template - template service
     * @param repository - repository service
     */
    public Pages(Template<T> template, Repository<T> repository) {
        this.template = template;
        this.repository = repository;
    }

	/**
	 * Get page by namespace, key and optional revision
	 * @param namespace - namespace of requested page
	 * @param key - key of requested page
	 * @param revision - optional revision id of requested page
	 * @return - page rendered with template service
	 */
    public F.Option<Content> get(String namespace, String key, F.Option<Long> revision) {

        // Get page content
        F.Option<T> content = repository.get(namespace, key, revision);

        // Check for missing
        if (content.isEmpty()) {
            return F.Option.None();
        }

        // Return rendered content
        return F.Option.Some(template.render(content.get()));
    }
    
    /**
     * Put page by namespace, key and optional revision
     * @param page
     * @param namespace
     * @param key
     * @param revision
     * @return
     */
    public void put(T page, String namespace, String key, F.Option<Long> revision) {
    
    	// Put into repository
    	repository.put(page, namespace, key, revision);
    	
    }

}
