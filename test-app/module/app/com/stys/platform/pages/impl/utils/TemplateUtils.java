package com.stys.platform.pages.impl.utils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import play.Application;

import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.Page;

public abstract class TemplateUtils  {

	public static final String TEMPLATES_KEY = "com.stys.platform.pages.templates";
	public static final String EDITOR_TEMPLATE_KEY = "com.stys.platform.pages.editor";
	
	/**
	 * Load templates from configuration
	 * @param application
	 * @return
	 */
	public static Map<String, Template<Page>> loadTemplates(Application application) {
		
		// Allocate 
		Map<String, Template<Page>> templates = new HashMap<>();
        
        // Load class names from configuration
        Map<String, Object> classes = application.configuration().getConfig(TEMPLATES_KEY).asMap();
        
        // Load classes
        for ( Map.Entry<String, Object> t : classes.entrySet() ) {
        	
        	try {
        	
        		// Load template class
        		@SuppressWarnings("unchecked")
				Template<Page> template = 
					(Template<Page>) application.classloader().loadClass((String) t.getValue()).newInstance();
        		
        		// Add to collection of templates
        		templates.put(t.getKey(), template);
        	
        	} catch (Exception ex) {
        		throw new RuntimeException(ex);
        	}
        }
        
        return templates;	
	
	}
	
	/**
	 * Load editor template from configuration 
	 * @param application
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Template<Page> loadEditorTemplate(Application application, Set<String> templates) {
		// Load editor template
        String name = application.configuration().getString(EDITOR_TEMPLATE_KEY);
        
        Template<Page> editor;

		try {

			// Load class
			Class<?> clazz = application.classloader().loadClass(name);

			// Get constructor
			Constructor<?> constructor = clazz.getConstructor(new Class[]{ Set.class });

			// Create instance
			editor = (Template<Page>) constructor.newInstance(templates);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} 
		
		return editor;
	}
	
}
