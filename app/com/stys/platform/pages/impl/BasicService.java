package com.stys.platform.pages.impl;

import play.libs.F;

import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;
import com.stys.platform.pages.utils.ResultUtils;

/**
 * Pages service. Implementors may consider to use Decorator pattern
 * to add aspects, such as access restrictions. 
 */
public class BasicService<T> implements Service<ContentResult, T> {

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
	 * @see com.stys.platform.show.PagesX#get(java.lang.String, java.lang.String, play.libs.F.Option)
	 */
    @Override
	public ContentResult get(String namespace, String key, F.Option<Long> revision) {

        // Get page content
        F.Option<T> page = repository.get(namespace, key, revision);

        // Check for missing
        if (page.isEmpty()) {
        	return ResultUtils.Forbidden(null);
        }

        // Return rendered content
        return ResultUtils.Ok(template.render(page.get()));

    }
    
    /* (non-Javadoc)
	 * @see com.stys.platform.show.PagesX#put(T, java.lang.String, java.lang.String, play.libs.F.Option)
	 */
    @Override
	public ContentResult put(T page, String namespace, String key, F.Option<Long> revision) {

    	// Put into repository
    	repository.put(page, namespace, key, revision);
    	
    	// Return Ok
    	return ResultUtils.Ok(null);

    }

}
