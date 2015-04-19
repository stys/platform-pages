package com.stys.platform.pages.impl.domain;

import com.stys.platform.pages.Service;
import play.twirl.api.Content;
import com.stys.platform.pages.Result;

public class ContentResultAdapter implements Service<ContentResult, Selector, Page> {

	private Service<Result<Content>, Selector, Page> wrapped;

	public ContentResultAdapter(Service<Result<Content>, Selector, Page> service) {
		this.wrapped = service;
	}

	@Override
	public ContentResult get(Selector selector) {

		final Result<Content> result = wrapped.get(selector);

		return new ContentResult() {
			@Override
			public Content getContent() {
				return result.getContent();
			}

			@Override
			public Status getStatus() {
				return result.getStatus();
			}
		};
	}

	@Override
	public ContentResult put(Selector selector, Page page) {
	
		final Result<Content> result = wrapped.put(selector, page);

		return new ContentResult() {
			@Override
			public Content getContent() {
				return result.getContent();
			}

			@Override
			public Status getStatus() {
				return result.getStatus();
			}
		};

	}
	
}
