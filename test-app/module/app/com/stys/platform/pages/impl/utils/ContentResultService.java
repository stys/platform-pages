package com.stys.platform.pages.impl.utils;

import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.Page;
import play.libs.F;
import play.mvc.Content;

import com.stys.platform.pages.Result;

public class ContentResultService implements Service<ContentResult, Page> {

	private Service<Result<Content>, Page> wrapped;

	public ContentResultService(Service<Result<Content>, Page> service) {
		this.wrapped = service;
	}

	@Override
	public ContentResult get(String namespace, String key, F.Option<Long> revision) {

		final Result<Content> result = wrapped.get(namespace, key, revision);

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
	public ContentResult put(Page page, String namespace, String key, F.Option<Long> revision) {
	
		final Result<Content> result = wrapped.put(page, namespace, key, revision);

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
