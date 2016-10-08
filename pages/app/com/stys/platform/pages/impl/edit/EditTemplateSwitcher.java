package com.stys.platform.pages.impl.edit;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.stys.platform.pages.impl.domain.*;
import play.Application;
import play.twirl.api.Content;

import com.stys.platform.pages.api.Result;
import com.stys.platform.pages.api.Results;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Render page: show editor or corresponding error
 */
public class EditTemplateSwitcher extends Results implements ContentService {

	private PageService delegate;

    private TemplateService templates;

	public EditTemplateSwitcher(
        @Named("edit:switcher:delegate") PageService delegate,
        @Named("edit:templates") TemplateService templates
    ) {
		this.delegate = delegate;
		this.templates = templates;
	}
	
	@Override
	public Result<Content> get(Selector selector) {
	
		// Retrieve page to edit
		Result<Page> result = this.delegate.get(selector);
	
		// Process result
		return map(result, templates.get(result.getStatus().name()).render(result.getContent()));
	}

	@Override
	public Result<Content> put(Selector selector, Page page) {
		
		// Delegate put
		Result<Page> result = delegate.put(selector, page);
	
		// Process result
		return map(result, templates.get(result.getStatus().name()).render(result.getContent()));
		
	}
	
}
