package com.stys.platform.pages.impl.edit;

import com.stys.platform.pages.api.*;
import play.twirl.api.Content;

import com.stys.platform.pages.api.Result;
import com.stys.platform.pages.api.Results;

import javax.inject.Inject;
import javax.inject.Named;

/** Applies template to page using edit templates */
public class EditTemplateService extends Results implements ContentService {

	private PageService source;

    private TemplateProvider templates;

	@Inject
	public EditTemplateService(
        @Named("edit:template:delegate") PageService source,
        @Named("edit:template-provider") TemplateProvider templates
    ) {
		this.source = source;
		this.templates = templates;
	}
	
	@Override
	public Result<Content> get(Selector selector) {
	
		// Retrieve page to edit
		Result<Page> result = this.source.get(selector);
	
		// Process result
		return map(result, templates.get(result.getStatus().name()).render(result.getPayload()));
	}

	@Override
	public Result<Content> put(Selector selector, Page page) {
		
		// Delegate put
		Result<Page> result = source.put(selector, page);
	
		// Process result
		return map(result, templates.get(result.getStatus().name()).render(result.getPayload()));
		
	}
	
}
