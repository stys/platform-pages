package com.stys.platform.pages.impl.view;

import java.util.Map;

import play.Application;
import play.Logger;

import com.stys.platform.pages.Plugin;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.Page;
import com.stys.platform.pages.impl.DatabasePagesRepository;
import com.stys.platform.pages.impl.TemplateUtils;

/**
 * Implementation of a simple show plugin with a predefined template and
 * backend database storage.
 */
public class DefaultViewPlugin extends ViewPlugin {

	/*
	 * Instance of application
	 */
	private Application application;
	
	/*
	 * Instance of show service
	 */
    private Service<Page> service;

    /**
     * Usual plugin constructor 
     */
    public DefaultViewPlugin(Application application) {
    	// Store reference to the application object
    	this.application = application;
    }

    /**
     * Plugin initialization. A composition root for show service.
     */
    @Override
    public void onStart() {

    	// Trace
        Logger.debug("Using %s", this.getClass().getSimpleName());
        
        // Load templates
        Map<String, Template<Page>> templates = TemplateUtils.loadTemplates(application);
        
        // Create template switcher
        Template<Page> switcher = new DefaultViewTemplate(templates);
        
        // Implementation of repository service
        Repository<Page> repository = new DatabasePagesRepository();

        // Create and store an instance of show service
        this.service = new DefaultViewService(switcher, repository);
    }

    /**
     * @see Plugin#getPagesService()
     */
    @Override
    public Service<Page> getPagesService() {
        // Return an instance
    	return this.service;
    }
    
}
