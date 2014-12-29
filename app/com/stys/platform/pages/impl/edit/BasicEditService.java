package com.stys.platform.pages.impl.edit;

import play.libs.F.Option;

import com.stys.platform.pages.Processor;
import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.ContentResult;
import com.stys.platform.pages.impl.Page;
import com.stys.platform.pages.utils.ResultUtils;

public class BasicEditService implements Service<ContentResult, Page> {

	private Service<ContentResult, Page> wrapped;

	private Template<Page> editor;
	
	private Processor<Page> processor;
	
	public BasicEditService(Service<ContentResult, Page> service, Template<Page> editor, Processor<Page> processor) {
	
		// create a basic show service
		this.wrapped = service;
		
		// keep reference to editor
		this.editor = editor;
			
		// store converter
		this.processor = processor;
	
	}
	
	@Override
	public ContentResult get(String namespace, String key, Option<Long> revision) {
	
		// Retrieve page for editting 
		ContentResult result = wrapped.get(namespace, key, revision);
	
		// If page is not found - we are creating a new one
		if( result.getStatus().equals(Result.Status.NotFound) ) {
			Page page = new Page();
			page.namespace = namespace;
			page.key = key;
			return ResultUtils.Ok(editor.render(page));
		}
		
		// Otherwise pass unchanged
		return result;
	}

	@Override
	public ContentResult put(Page page, String namespace, String key, Option<Long> revision) {
		
		// execute processor, then put
		page = this.processor.process(page);
		
		// deligate
		return wrapped.put(page, namespace, key, revision);		
	
	}
	
}
