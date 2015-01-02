package com.stys.platform.pages.impl;

import java.util.Map;

import play.libs.F.Option;
import play.mvc.Content;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Results;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;

public class ErrorHandlingService implements Service<Result<Content>, Page>  {

	// Wrapped service instance
	private Service<Result<Content>, Page> wrapped; 		
	
	// Registered handlers for erros
	private Map<Result.Status, Template<Page>> templates;
	
	// Instance of results utility
	private Results<Content> results;
	
	public ErrorHandlingService(Service<Result<Content>, Page> service, Map<Result.Status, Template<Page>> templates) {
		this.wrapped = service;
		this.templates = templates;
	}

	@Override
	public Result<Content> get(String namespace, String key, Option<Long> revision) {
		
		// Deligate to wrapped service
		Result<Content> result = wrapped.get(namespace, key, revision);
		
		// Create a stub page
		Page stub = new Page();
		stub.namespace = namespace;
		stub.key = key;
				
		// Handle errors
		return handle(result, stub);
		
	}

	@Override
	public Result<Content> put(Page page, String namespace, String key, Option<Long> revision) {
		
		// Deligate to wrapping service
		Result<Content> result = wrapped.put(page, namespace, key, revision);

		// Handle errors
		return handle(result, page);
		
	}
	
	private Result<Content> handle(Result<Content> previous, Page page) {
		
		// Check results
		if (previous.getStatus().equals(Result.Status.Ok)) {
			
			// Return unmodified
			return previous;
		
		} else {
			
			// Look for a handler
			Template<Page> template = templates.get(previous.getStatus());
			
			// If handler is not present - return unmodified
			if( null == template ) {
				return previous;
			}
			
			// Transform
			return results.map(previous, template.render(page));
			
		}
	
	}
	
}
