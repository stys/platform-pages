package com.stys.platform.pages.impl.edit;

import java.util.Map;

import play.Application;
import play.Logger;

import com.stys.platform.pages.Processor;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.*;
import com.stys.platform.pages.utils.AccessUtils;
import com.stys.platform.pages.utils.TemplateUtils;

public class DefaultEditPlugin extends EditPlugin {
	
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
        Logger.debug("Using %s", this.getClass().getSimpleName());
        
        // Load view templates
        Map<String, Template<Page>> templates = TemplateUtils.loadTemplates(application);
  
        // Load editor template
        Template<Page> editor = TemplateUtils.loadEditorTemplate(application, templates.keySet());
        
        // Implementation of repository service
		Repository<Page> repository = new DatabasePagesRepository();

		// Implementation of processor
		Processor<Page> converter = new MarkdownHtmlConverter();
		
		// Access manager
		Service<ContentResult, Page> access = AccessUtils.getEditAccessManager(application);
		
		// Basic service
		Service<ContentResult, Page> pages = new BasicService<>(editor, repository);
		
		// Edit service
		Service<ContentResult, Page> edit = new BasicEditService(pages, editor, converter);
		
		// Access managed service
		Service<ContentResult, Page> managed = new AccessManagedService(edit, access);
		
        // Create and store an instance of show service
        this.service = managed;
		
	}
	
	@Override
	public Service<ContentResult, Page> getPagesService() {
		return this.service;
	}
	
}
