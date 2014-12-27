package com.stys.platform.pages.impl.edit;

import java.util.Map;

import play.Application;
import play.Logger;

import com.stys.platform.pages.Processor;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.MarkdownHtmlConverter;
import com.stys.platform.pages.impl.Page;
import com.stys.platform.pages.impl.DatabasePagesRepository;
import com.stys.platform.pages.impl.TemplateUtils;

public class DefaultEditPlugin extends EditPlugin {
	
	/*
	 * Instance of application
	 */
	private Application application;
	
	/*
	 * Instance of service
	 */
	private Service<Page> service;
	
	public DefaultEditPlugin(Application application) {
		this.application = application;
	}
	
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
		
        // Create and store an instance of pages service
        this.service = new DefaultEditService(editor, repository, converter);
		
	}
	
	@Override
	public Service<Page> getPagesService() {
		return this.service;
	}
	
}
