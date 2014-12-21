package com.stys.platform.pages.impl;

import java.util.Map;

import play.Application;
import play.Logger;

import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Template;

public class EditPagesPlugin extends PagesPlugin {

	private static final String EDITOR_TEMPLATES_KEY = "com.stys.platform.pages.editor";
	
	private PagesService pages;
	
	public EditPagesPlugin(Application application) {
		super(application);
	}
	
	public void onStart() {
	
		// Trace
        Logger.debug("Using %s", this.getClass().getSimpleName());
        
        // Load view templates
        Map<String, Template<Page>> templates = this.templates();
        
        // Load editor template
        String name = application.configuration().getString(EDITOR_TEMPLATES_KEY);
        
        Template<PageEdit> editor;
		try {
			@SuppressWarnings("unchecked")
			Template<PageEdit> _editor = (Template<PageEdit>) application.classloader().loadClass(name).newInstance();
			editor = _editor;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} 
        
        // Create editor 
        EditorTemplate wrapper = new EditorTemplate(editor, templates);
        
        // Implementation of repository service
        Repository<Page> repository = new PagesRepository();

        // Create and store an instance of pages service
        this.pages = new PagesService(wrapper, repository);
		
	}
	
	@Override
	public PagesService getPagesService() {
		return this.pages;
	}
	
}
