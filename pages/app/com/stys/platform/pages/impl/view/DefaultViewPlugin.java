package com.stys.platform.pages.impl.view;

import java.lang.reflect.Constructor;

import play.Application;
import play.Logger;
import play.twirl.api.Content;

import com.stys.platform.pages.Plugin;
import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.ContentResult;
import com.stys.platform.pages.impl.domain.ContentResultAdapter;
import com.stys.platform.pages.impl.domain.Page;

/**
 * Implementation of a simple show plugin with a predefined template and
 * backend database storage.
 */
public class DefaultViewPlugin extends ViewPlugin {

	private static final String TEMPLATE_SWITCHER_KEY = "com.stys.platform.pages.view.template.switcher";
	private static final String ACCESS_MANAGER_KEY = "com.stys.platform.pages.view.access.manager";
	private static final String PROCESSOR_KEY = "com.stys.platform.pages.view.processor";
	private static final String REPOSITORY_KEY = "com.stys.platform.pages.view.repository";
	
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

    	 // Load services
        try {
        	
        	// Repository
        	String name = this.application.configuration().getString(REPOSITORY_KEY);
        	Logger.debug(String.format("Picked %s", name));
        	Class<?> clazz = this.application.classloader().loadClass(name);
        	Constructor<?> constructor = clazz.getConstructor(Application.class, Service.class);
        	@SuppressWarnings("unchecked")
			Service<Result<Page>, Page> repository = 
				(Service<Result<Page>, Page>) constructor.newInstance(this.application, null);
        	
        	// Processor
        	name = this.application.configuration().getString(PROCESSOR_KEY);
        	Logger.debug(String.format("Picked %s", name));
        	clazz = this.application.classloader().loadClass(name);
        	constructor = clazz.getConstructor(Application.class, Service.class);
        	@SuppressWarnings("unchecked")
			Service<Result<Page>, Page> processor = 
				(Service<Result<Page>, Page>) constructor.newInstance(this.application, repository);
        	
        	// Access manager
        	name = this.application.configuration().getString(ACCESS_MANAGER_KEY);
        	Logger.debug(String.format("Picked %s", name));
        	clazz = this.application.classloader().loadClass(name);
        	constructor = clazz.getConstructor(Application.class, Service.class);
        	@SuppressWarnings("unchecked")
			Service<Result<Page>, Page> manager = 
				(Service<Result<Page>, Page>) constructor.newInstance(this.application, processor);
        	
        	// Template switcher
        	name = this.application.configuration().getString(TEMPLATE_SWITCHER_KEY);
        	Logger.debug(String.format("Picked %s", name));
        	clazz = this.application.classloader().loadClass(name);
        	constructor = clazz.getConstructor(Application.class, Service.class);
        	@SuppressWarnings("unchecked")
			Service<Result<Content>, Page> switcher = 
				(Service<Result<Content>, Page>) constructor.newInstance(this.application, manager);
        	
        	// Assembly
        	this.service = new ContentResultAdapter(switcher);
        	
        } catch(Exception ex) {
        	throw new RuntimeException(ex);        	        	
        }
    	
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
