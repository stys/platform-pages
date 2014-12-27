package com.stys.platform.pages.impl;

import com.stys.platform.pages.Service;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Template;

import play.libs.F;
import play.mvc.Content;

/**
 * Pages service. Implementors may consider to use Decorator pattern
 * to add aspects, such as access restrictions. 
 */
public class BasicService<T> implements Service<T> {

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
    public BasicService(Template<T> template, Repository<T> repository) {
        this.template = template;
        this.repository = repository;
    }

	/* (non-Javadoc)
	 * @see com.stys.platform.pages.PagesX#get(java.lang.String, java.lang.String, play.libs.F.Option)
	 */
    @Override
	public F.Option<Content> get(String namespace, String key, F.Option<Long> revision) {

        // Get page content
        F.Option<T> page = repository.get(namespace, key, revision);

        // Check for missing
        if (page.isEmpty()) {
            return F.Option.None();
        }

        // Return rendered content
        return F.Option.Some(template.render(page.get()));

    }
    
    /* (non-Javadoc)
	 * @see com.stys.platform.pages.PagesX#put(T, java.lang.String, java.lang.String, play.libs.F.Option)
	 */
    @Override
	public void put(T page, String namespace, String key, F.Option<Long> revision) {

    	// Put into repository
    	repository.put(page, namespace, key, revision);

    }

}
