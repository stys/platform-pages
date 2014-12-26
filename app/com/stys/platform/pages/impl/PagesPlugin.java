package com.stys.platform.pages.impl;

import java.util.HashMap;
import java.util.Map;

import play.Application;

import com.stys.platform.pages.Template;

/**
 * Resolvable name for implementations of {@link PagesPlugin}
 */
public abstract class PagesPlugin extends com.stys.platform.pages.Plugin<Page> {

	public static final String TEMPLATES_KEY = "com.stys.platform.pages.templates";
	
	public abstract PagesService getPagesService();
	
	protected Application application;
		
	public PagesPlugin(Application application) {
		this.application = application;		
	}
	
	public Map<String, Template<Page>> loadTemplates() {
		
		// Allocate 
		Map<String, Template<Page>> templates = new HashMap<>();
        
        // Load class names from configuration
        Map<String, Object> classes = this.application.configuration().getConfig(TEMPLATES_KEY).asMap();
        
        // Load classes
        for ( Map.Entry<String, Object> t : classes.entrySet() ) {
        	
        	try {
        	
        		// Load template class
        		@SuppressWarnings("unchecked")
				Template<Page> template = (Template<Page>) application.classloader().loadClass((String) t.getValue()).newInstance();
        		
        		// Add to collection of templates
        		templates.put(t.getKey(), template);
        	
        	} catch (Exception ex) {
        		throw new RuntimeException(ex);
        	}
        }
        
        return templates;
		
	}
	
}
