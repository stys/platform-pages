package com.stys.platform.pages.impl.view;

import java.util.Map;

import com.stys.platform.pages.*;
import com.stys.platform.pages.impl.utils.ContentResult;
import com.stys.platform.pages.impl.utils.ContentResultService;
import play.Application;
import play.Logger;

import com.stys.platform.pages.impl.*;
import com.stys.platform.pages.impl.utils.AccessUtils;
import com.stys.platform.pages.impl.utils.TemplateUtils;
import play.mvc.Content;

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
    private Service<ContentResult, Page> service;

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
        Logger.debug(String.format("picked %s", this.getClass().getSimpleName()));
        
        // Load templates
        Map<String, Template<Page>> templates = TemplateUtils.loadTemplates(application);
        
        // Create template switcher
        Template<Page> switcher = new TemplateSwitcher(templates);
        
        // Implementation of repository service
        Repository<Page> repository = new DatabasePagesRepository();

        // Basic service
        Service<Result<Content>, Page> basic = new BasicService<>(switcher, repository);
        
        // Access manager
        Service<Result<Content>, Page> access = AccessUtils.getViewAccessManager(application);
        
        // Compose
        Service<Result<Content>, Page> managed = new AccessManagedService(basic, access);
        
        // Convert to ContentResult
        Service<ContentResult, Page> wrapped = new ContentResultService(managed);

        // Create and store an instance of show service
        this.service = wrapped;

    }

    /**
     * @see Plugin#getPagesService()
     */
    @Override
    public Service<ContentResult, Page> getPagesService() {
        // Return an instance
    	return this.service;
    }
    
}
