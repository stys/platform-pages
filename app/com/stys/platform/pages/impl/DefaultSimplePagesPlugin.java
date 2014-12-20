package com.stys.platform.pages.impl;

import java.util.HashMap;
import java.util.Map;

import play.Application;
import play.Logger;

import com.stys.platform.pages.Plugin;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Template;

/**
 * Implementation of a simple pages plugin with a predefined template and
 * backend database storage.
 */
public class DefaultSimplePagesPlugin extends SimplePagesPlugin {

	public static final String TEMPLATES_KEY = "com.stys.platform.pages.templates";

	/*
	 * Instance of the application
	 */
	private Application application;
	
	/*
	 * Instance of pages service
	 */
    private SimplePages pages;

    /**
     * Usual plugin constructor 
     */
    public DefaultSimplePagesPlugin(Application application) {
    	// Store reference to the application object
    	this.application = application;
    }

    /**
     * Plugin initialization. A composition root for pages service.
     */
    @Override
    public void onStart() {

    	// Trace
        Logger.debug("Using %s", this.getClass().getSimpleName());
        
        // Collection of templates
        Map<String, Template<String>> templates = new HashMap<>();
        
        // Load class names from configuration
        Map<String, Object> classes = this.application.configuration().getConfig(TEMPLATES_KEY).asMap();
        
        // Load classes
        for ( Map.Entry<String, Object> t : classes.entrySet() ) {
        	
        	try {
        	
        		// Load template class
        		@SuppressWarnings("unchecked")
				Template<String> template = (Template<String>) application.classloader().loadClass((String) t.getValue()).newInstance();
        		
        		// Add to collection of templates
        		templates.put(t.getKey(), template);
        	
        	} catch (Exception ex) {
        		throw new RuntimeException(ex);
        	}
        }
        
        // Create template switcher
        TemplateSwitcher skinner = new TemplateSwitcher(templates);
        
        // Implementation of repository service
        Repository<SimplePage> repository = new DefaultSimplePagesRepository();

        // Create and store an instance of pages service
        this.pages = new SimplePages(skinner, repository);
    }

    /**
     * @see Plugin#getPagesService()
     */
    @Override
    public SimplePages getPagesService() {
        // Return an instance
    	return this.pages;
    }
    
}
