package com.stys.platform.pages.impl;

import com.stys.platform.pages.api.*;
import play.twirl.api.Content;

import javax.inject.Inject;
import javax.inject.Named;

import static com.stys.platform.pages.api.TemplateKeys.ERROR_TEMPLATE_KEY;

/** Render page: show editor or corresponding error */
public class ViewTemplateService extends Results implements ContentService {

	private PageService source;

    private TemplateProvider provider;

    @Inject
	public ViewTemplateService(
        @Named("view:template:delegate") PageService source,
        @Named("view:template-provider") TemplateProvider provider
    ) {
	    this.source = source;
		this.provider = provider;
	}
	
	@Override
	public Result<Content> get(Selector selector) {
	
		// Retrieve page to edit
		final Result<Page> result = this.source.get(selector);

		Template template;
		if(result.getStatus().equals(Result.Status.Ok)) {
			template = provider.get(result.getPayload().template);
		} else {
			template = provider.get(ERROR_TEMPLATE_KEY);
		}

		return map(result, template.render(result.getPayload()));
	}

	@Override
	public Result<Content> put(Selector selector, Page page) {
		// Not allowed
        throw new UnsupportedOperationException("ViewTemplateService does not support put operation");
	}
	
}
