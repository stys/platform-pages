package com.stys.platform.pages.impl.view;

import com.stys.platform.pages.api.*;
import play.twirl.api.Content;

import javax.inject.Inject;
import javax.inject.Named;

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
	
		// Ok
		if( result.getStatus().equals(Result.Status.Ok)) {
			// Get template
			Template template = provider.get(result.getPayload().template);
			// Default template			
			if (null == template) {
				Content content = new Content() {
					@Override
					public String body() {
						return result.getPayload().content;
					}

					@Override
					public String contentType() {
						return "text/plain";
					}
				};
				return Ok(content);
			} else {
				return Ok(template.render(result.getPayload()));
			}
		}
		
		// Process errors
		return map(result, provider.get(result.getStatus().name()).render(result.getPayload()));
	}

	@Override
	public Result<Content> put(Selector selector, Page page) {
		// Not allowed
        throw new UnsupportedOperationException("ViewTemplateService does not support put operation");
	}
	
}
