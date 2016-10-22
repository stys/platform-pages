package com.stys.platform.pages.impl;

import com.stys.platform.pages.api.*;
import play.twirl.api.Content;

import com.stys.platform.pages.api.Result;
import com.stys.platform.pages.api.Results;

import javax.inject.Inject;
import javax.inject.Named;

import static com.stys.platform.pages.api.TemplateKeys.EDITOR_TEMPLATE_KEY;
import static com.stys.platform.pages.api.TemplateKeys.ERROR_TEMPLATE_KEY;

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
		Result<Page> result = this.source.get(selector);
		return map(result, render(result.getStatus(), result.getPayload()));
	}

	@Override
	public Result<Content> put(Selector selector, Page page) {
		Result<Page> result = source.put(selector, page);
		return map(result, render(result.getStatus(), result.getPayload()));
		
	}

    private Content render(Result.Status status, Page page) {
        // Process result
        Template template;
        if (status == Result.Status.Ok) {
            template = templates.get(EDITOR_TEMPLATE_KEY);
        } else {
            template = templates.get(ERROR_TEMPLATE_KEY);
            page.content = status.name();
        }
        return template.render(page);
    }

}
