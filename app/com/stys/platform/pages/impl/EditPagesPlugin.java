package com.stys.platform.pages.impl;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

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
        Map<String, Template<Page>> templates = this.loadTemplates();
        
        // Load editor template
        String name = application.configuration().getString(EDITOR_TEMPLATES_KEY);
        
        Template<Page> editor;

		try {

			// Load class
			Class clazz = application.classloader().loadClass(name);

			// Get constructor
			Constructor constructor = clazz.getConstructor(new Class[]{ Set.class });

			// Create instance
			editor = (Template<Page>) constructor.newInstance(templates.keySet());

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} 
        
        // Create editor 
        EditorTemplate wrapper = new EditorTemplate(editor);
        
        // Implementation of repository service
        Repository<Page> wrapped = new PagesRepository();
		Repository<Page> repository = new ConvertingPagesRepository(wrapped);

        // Create and store an instance of pages service
        this.pages = new PagesService(wrapper, repository);
		
	}
	
	@Override
	public PagesService getPagesService() {
		return this.pages;
	}
	
}
