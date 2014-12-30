package com.stys.platform.pages.impl.edit;

import play.libs.F.Option;

import com.stys.platform.pages.*;
import com.stys.platform.pages.impl.Page;

import play.mvc.Content;


public class BasicEditService implements Service<Result<Content>, Page> {

	private Service<Result<Content>, Page> wrapped;

	private Template<Page> editor;
	
	private Processor<Page> processor;

	private Results<Content> results = new Results<>();
	
	public BasicEditService(Service<Result<Content>, Page> service, Template<Page> editor, Processor<Page> processor) {
	
		// create a basic show service
		this.wrapped = service;
		
		// keep reference to editor
		this.editor = editor;
			
		// store converter
		this.processor = processor;
	
	}
	
	@Override
	public Result<Content> get(String namespace, String key, Option<Long> revision) {
	
		// Retrieve page to edit
		Result<Content> result = wrapped.get(namespace, key, revision);
	
		// If page is not found - we are creating a new one
		if( result.getStatus().equals(Result.Status.NotFound) ) {
			Page page = new Page();
			page.namespace = namespace;
			page.key = key;
			return results.Ok(editor.render(page));
		}
		
		// Otherwise pass unchanged
		return result;
	}

	@Override
	public Result<Content> put(Page page, String namespace, String key, Option<Long> revision) {
		
		// execute processor, then put
		page = this.processor.process(page);
		
		// delegate
		return wrapped.put(page, namespace, key, revision);		
	
	}
	
}
