package com.stys.platform.pages.impl;

import com.stys.platform.pages.api.*;
import play.twirl.api.Content;

import com.stys.platform.pages.api.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.stys.platform.pages.api.TemplateKeys.EDITOR_TEMPLATE_KEY;
import static com.stys.platform.pages.api.TemplateKeys.ERROR_TEMPLATE_KEY;

@Singleton
public class DefaultEditService implements Service<Result<Content>, Selector, Page> {

    public interface Delegate extends Service<Result<Page>, Selector, Page> {}

    public interface EditTemplateProvider extends TemplateProvider {}

	private Service<Result<Page>, Selector, Page> delegate;

	private TemplateProvider templates;

	@Inject
	public DefaultEditService(Delegate delegate, EditTemplateProvider templates) {
		this.delegate = delegate;
		this.templates = templates;
	}
	
	@Override
	public Result<Content> get(Selector selector) {
		Result<Page> result = this.delegate.get(selector);
		return result.map(r -> render(r.getStatus(), r.getPayload()));
	}

	@Override
	public Result<Content> put(Selector selector, Page page) {
		Result<Page> result = delegate.put(selector, page);
		return result.map(r -> render(r.getStatus(), r.getPayload()));
	}

    private Content render(Result.Status status, Page page) {
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
