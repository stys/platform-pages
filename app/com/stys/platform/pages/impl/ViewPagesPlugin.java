package com.stys.platform.pages.impl;

import play.Application;
import play.Logger;

import com.stys.platform.pages.Plugin;
import com.stys.platform.pages.Repository;

/**
 * Implementation of a simple pages plugin with a predefined template and
 * backend database storage.
 */
public class ViewPagesPlugin extends PagesPlugin {

	/*
	 * Instance of pages service
	 */
    private PagesService pages;

    /**
     * Usual plugin constructor 
     */
    public ViewPagesPlugin(Application application) {
    	// Store reference to the application object
    	super(application);
    }

    /**
     * Plugin initialization. A composition root for pages service.
     */
    @Override
    public void onStart() {

    	// Trace
        Logger.debug("Using %s", this.getClass().getSimpleName());
                
        // Create template switcher
        ViewTemplateSwitcher switcher = new ViewTemplateSwitcher(templates());
        
        // Implementation of repository service
        Repository<Page> repository = new PagesRepository();

        // Create and store an instance of pages service
        this.pages = new PagesService(switcher, repository);
    }

    /**
     * @see Plugin#getPagesService()
     */
    @Override
    public PagesService getPagesService() {
        // Return an instance
    	return this.pages;
    }
    
}
