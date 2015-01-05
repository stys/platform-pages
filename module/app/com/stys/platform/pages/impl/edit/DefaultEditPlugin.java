package com.stys.platform.pages.impl.edit;

import java.lang.reflect.Constructor;

import play.Application;
import play.Logger;
import play.mvc.Content;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.ContentResult;
import com.stys.platform.pages.impl.domain.ContentResultAdapter;
import com.stys.platform.pages.impl.domain.Page;

public class DefaultEditPlugin extends EditPlugin {
	
	private static final String TEMPLATE_SWITCHER_KEY = "com.stys.platform.pages.edit.template.switcher";
	private static final String ACCESS_MANAGER_KEY = "com.stys.platform.pages.edit.access.manager";
	private static final String PROCESSOR_KEY = "com.stys.platform.pages.edit.processor";
	private static final String REPOSITORY_KEY = "com.stys.platform.pages.edit.repository";
	
	/*
	 * Instance of application
	 */
	private Application application;
	
	/*
	 * Instance of service
	 */
	private Service<ContentResult, Page> service;
	
	public DefaultEditPlugin(Application application) {
		this.application = application;
	}
	
	/**
	 * Composition root
	 */
	public void onStart() {
	
		// Trace
        Logger.debug(String.format("picked %s", this.getClass().getSimpleName()));
  
        // Load services
        try {
        	
        	// Repository
        	String name = this.application.configuration().getString(REPOSITORY_KEY);
        	Class<?> clazz = this.application.classloader().loadClass(name);
        	Constructor<?> constructor = clazz.getConstructor(Application.class, Service.class);
        	@SuppressWarnings("unchecked")
			Service<Result<Page>, Page> repository = 
				(Service<Result<Page>, Page>) constructor.newInstance(this.application, null);
        	Logger.debug(String.format("Picked %s", name));
        	
        	// Processor
        	name = this.application.configuration().getString(PROCESSOR_KEY);
        	clazz = this.application.classloader().loadClass(name);
        	constructor = clazz.getConstructor(Application.class, Service.class);
        	@SuppressWarnings("unchecked")
			Service<Result<Page>, Page> processor = 
				(Service<Result<Page>, Page>) constructor.newInstance(this.application, repository);
        	Logger.debug(String.format("Picked %s", name));
        	
        	// Access manager
        	name = this.application.configuration().getString(ACCESS_MANAGER_KEY);
        	clazz = this.application.classloader().loadClass(name);
        	constructor = clazz.getConstructor(Application.class, Service.class);
        	@SuppressWarnings("unchecked")
			Service<Result<Page>, Page> manager = 
				(Service<Result<Page>, Page>) constructor.newInstance(this.application, processor);
        	Logger.debug(String.format("Picked %s", name));
        	
        	// Template switcher
        	name = this.application.configuration().getString(TEMPLATE_SWITCHER_KEY);
        	clazz = this.application.classloader().loadClass(name);
        	constructor = clazz.getConstructor(Application.class, Service.class);
        	@SuppressWarnings("unchecked")
			Service<Result<Content>, Page> switcher = 
				(Service<Result<Content>, Page>) constructor.newInstance(this.application, manager);
        	Logger.debug(String.format("Picked %s", name));
        	
        	// Assembly
        	this.service = new ContentResultAdapter(switcher);
        	
        } catch(Exception ex) {
        	throw new RuntimeException(ex);        	        	
        }
		
	}

	@Override
	public Service<ContentResult, Page> getPagesService() {
		return this.service;
	}
	
		
}
