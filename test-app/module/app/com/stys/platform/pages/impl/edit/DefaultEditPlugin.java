package com.stys.platform.pages.impl.edit;

import java.util.Map;

import play.Application;
import play.Logger;
import play.mvc.Content;

import com.stys.platform.pages.Processor;
import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.*;
import com.stys.platform.pages.impl.utils.AccessUtils;
import com.stys.platform.pages.impl.utils.ContentResult;
import com.stys.platform.pages.impl.utils.ContentResultService;
import com.stys.platform.pages.impl.utils.TemplateUtils;

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
        Logger.debug(String.format("picked %s", this.getClass().getSimpleName()));
        
        // Load view templates
        Map<String, Template<Page>> templates = TemplateUtils.loadTemplates(application);
  
        // Load editor template
        Template<Page> editor = TemplateUtils.loadEditorTemplate(application, templates.keySet());
        
        // Implementation of repository service
		Service<Result<Page>, Page> repository = new DatabasePagesRepository();

		// Implementation of processor
		Processor<Page> converter = new MarkdownHtmlConverter();
		
		// Access manager
		Service<Result<Content>, Page> access = AccessUtils.getEditAccessManager(application);
		
		// Basic service
		Service<Result<Content>, Page> pages = new BasicService<>(editor, repository);
		
		// Edit service
		Service<Result<Content>, Page> edit = new BasicEditService(pages, editor, converter);
		
		// Access managed service
		Service<Result<Content>, Page> managed = new AccessManagedService<Content, Page>(edit, access);

		// Convert to ContentResult
		Service<ContentResult, Page> wrapped = new ContentResultService(managed);
		
        // Create and store an instance of show service
        this.service = wrapped;
		
	}
	
	@Override
	public Service<ContentResult, Page> getPagesService() {
		return this.service;
	}
	
}
