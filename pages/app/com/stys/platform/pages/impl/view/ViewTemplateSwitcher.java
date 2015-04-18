package com.stys.platform.pages.impl.view;

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
public class ViewTemplateSwitcher<S> extends Results implements Service<Result<Content>, S, Page> {

	private static final String TEMPLATES_KEY = "com.stys.platform.pages.templates";
	private static final String BAD_REQUEST_KEY = "com.stys.platform.pages.bad_request";
	private static final String UNAUTHORIZED_KEY = "com.stys.platform.pages.unauthorized";
	private static final String FORBIDDEN_KEY = "com.stys.platform.pages.forbidden";
	private static final String NOT_FOUND_KEY = "com.stys.platform.pages.not_found";
	
	private Service<Result<Page>, S, Page> delegate;
	
	@SuppressWarnings("unused")
	private Application application;
	
	private Map<String, Template> templates;
	
	private Map<Result.Status, Template> errors;

	public ViewTemplateSwitcher(Application application, Service<Result<Page>, S, Page> delegate) {
	
		// Store references
		this.delegate = delegate;
		this.application = application;
		
		// Load templates
		this.templates = loadTemplates(application);
		
		// Load error templates
		this.errors = loadErrorTemplates(application);
		
	}
	
	@Override
	public Result<Content> get(S selector) {
	
		// Retrieve page to edit
		final Result<Page> result = this.delegate.get(selector);
	
		// Ok
		if( result.getStatus().equals(Result.Status.Ok)) {
			// Get template
			Template template = templates.get(result.getContent().template);
			// Default template			
			if (null == template) {
				Content content = new Content() {
					@Override
					public String body() {
						return result.getContent().content;
					}

					@Override
					public String contentType() {
						return "text/plain";
					}
				};
				return Ok(content);
			} else {
				return Ok(template.render(result.getContent()));
			}
		}
		
		// Process errors
		return map(result, errors.get(result.getStatus()).render(result.getContent()));
	}

	@Override
	public Result<Content> put(S selector, Page page) {
		
		// Delegate put
		final Result<Page> result = delegate.put(selector, page);
	
		// Ok
		if( result.getStatus().equals(Result.Status.Ok)) {
			// Get template
			Template template = templates.get(result.getContent().template);
			// Default template			
			if (null == template) {
				Content content = new Content() {
					@Override
					public String body() {
						return result.getContent().content;
					}

					@Override
					public String contentType() {
						return "text/plain";
					}
				};
				return Ok(content);
			} else {
				return Ok(template.render(result.getContent()));
			}
		}
		
		// Process errors
		return map(result, errors.get(result.getStatus()).render(result.getContent()));
		
	}

	private static Map<String, Template> loadTemplates(Application application) {
		// Allocate 
		Map<String, Template> templates = new HashMap<>();
        
        // Load class names from configuration
        Map<String, Object> classes = application.configuration().getConfig(TEMPLATES_KEY).asMap();
        
        // Load classes
        for ( Map.Entry<String, Object> t : classes.entrySet() ) {
        	
        	try {
        	
        		// Load template class
				Template template = (Template) application.classloader().loadClass((String) t.getValue()).newInstance();
        		
        		// Add to collection of templates
        		templates.put(t.getKey(), template);
        	
        	} catch (Exception ex) {
        		throw new RuntimeException(ex);
        	}
        }
        
        return templates;	
	}
	
	private static Map<Result.Status, Template> loadErrorTemplates(Application application) {
	
		// Allocate result
		Map<Result.Status, Template> templates = new EnumMap<>(Result.Status.class);
				
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
