package com.stys.platform.pages.impl;

import com.stys.platform.pages.api.*;
import play.twirl.api.Content;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.stys.platform.pages.api.TemplateKeys.ERROR_TEMPLATE_KEY;

@Singleton
public class DefaultViewService implements Service<Result<Content>, Selector, Page> {

	public interface Delegate extends Service<Result<Page>, Selector, Page> {}

	private Service<Result<Page>, Selector, Page> delegate;

    public interface ViewTemplateProvider extends TemplateProvider {}

    private TemplateProvider provider;

    @Inject
	public DefaultViewService(Delegate delegate, ViewTemplateProvider provider) {
	    this.delegate = delegate;
		this.provider = provider;
	}
	
	@Override
	public Result<Content> get(Selector selector) {
	
		// Retrieve page to edit
		final Result<Page> result = this.delegate.get(selector);

		Template template;
		if(result.getStatus().equals(Result.Status.Ok)) {
			template = provider.get(result.getPayload().template);
		} else {
			template = provider.get(ERROR_TEMPLATE_KEY);
		}

		return result.map(r -> template.render(r.getPayload()));
	}

	@Override
	public Result<Content> put(Selector selector, Page page) {
        throw new UnsupportedOperationException();
	}
	
}
