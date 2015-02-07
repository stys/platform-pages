package com.stys.platform.pages.impl.edit;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import play.Application;
import play.libs.F.Option;
import play.twirl.api.Content;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Results;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.Template;

/**
 * Render page: show editor or corresponding error
 */
public class EditTemplateSwitcher extends Results implements Service<Result<Content>, Page> {

	private static final String EDITOR_KEY = "com.stys.platform.pages.editor";
	private static final String BAD_REQUEST_KEY = "com.stys.platform.pages.bad_request";
	private static final String UNAUTHORIZED_KEY = "com.stys.platform.pages.unauthorized";
	private static final String FORBIDDEN_KEY = "com.stys.platform.pages.forbidden";
	private static final String NOT_FOUND_KEY = "com.stys.platform.pages.not_found";
	
	private Service<Result<Page>, Page> delegate;
	
	@SuppressWarnings("unused")
	private Application application;
	
	private Map<Result.Status, Template> templates;

	public EditTemplateSwitcher(Application application, Service<Result<Page>, Page> delegate) {
	
		// Store references
		this.delegate = delegate;
		this.application = application;
		
		// Load templates
		this.templates = loadTemplates(application);
	}
	
	@Override
	public Result<Content> get(String namespace, String key, Option<Long> revision) {
	
		// Retrieve page to edit
		Result<Page> result = this.delegate.get(namespace, key, revision);
	
		// Process result
		return map(result, templates.get(result.getStatus()).render(result.getContent()));
	}

	@Override
	public Result<Content> put(Page page, String namespace, String key, Option<Long> revision) {
		
		// Delegate put
		Result<Page> result = delegate.put(page, namespace, key, revision);		
	
		// Process result
		return map(result, templates.get(result.getStatus()).render(result.getContent()));
		
	}

	private static Map<Result.Status, Template> loadTemplates(Application application) {
	
		// Allocate result
		Map<Result.Status, Template> templates = new EnumMap<>(Result.Status.class);
		
		// Load editor
		try {
			// Get editor class name from configuration
			String name = application.configuration().getString(EDITOR_KEY);
			// Throw if missing
			if (null == name) throw new IllegalStateException(String.format("Configuration key %s is not defined", EDITOR_KEY));
			// Load class and create an instance
			Template editor = (Template) application.classloader().loadClass(name).newInstance();
			// Put into collection of templates
			templates.put(Result.Status.Ok, editor);		
		} catch (Exception ex) {
			// Can't continue without editor
			throw new RuntimeException(ex);
		}	
		
		// Load error templates
		Map<String, String> errors = new HashMap<>();
		errors.put(Result.Status.BadRequest.name(), BAD_REQUEST_KEY);
		errors.put(Result.Status.Unauthorized.name(), UNAUTHORIZED_KEY);
		errors.put(Result.Status.Forbidden.name(), FORBIDDEN_KEY);
		errors.put(Result.Status.NotFound.name(), NOT_FOUND_KEY);
		
		for ( final Map.Entry<String, String> e : errors.entrySet() ) {
		
			try {
				// Get name of class
				String clazz = application.configuration().getString(e.getValue());
				// Load class and create an instance
				Template template = (Template) application.classloader().loadClass(clazz).newInstance(); 
				// Put into collection of templates
				templates.put(Result.Status.valueOf(e.getKey()), template);
			} catch(Exception ex) {
				// Default template
				templates.put(Result.Status.valueOf(e.getKey()), new Template() {
					@Override
					public Content render(Page page) {
						return new Content() {
							@Override
							public String body() {
								return e.getKey();
							}
							@Override
							public String contentType() {
								return "text/plain";
							}
						};
					}
				});
			}
			
		}
		
		// Done
		return templates;
	}
	
}
