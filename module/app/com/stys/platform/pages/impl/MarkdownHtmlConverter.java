package com.stys.platform.pages.impl;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

import play.Application;
import play.libs.F.Option;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.Page;

public class MarkdownHtmlConverter implements Service<Result<Page>, Page> {
	
	/**
	 * Instance of wrapped service
	 */
	private Service<Result<Page>, Page> wrapped;		

	/**
	 * Instance of applicaton
	 */
	@SuppressWarnings("unused")
	private Application application;	
	
	/**
	 * Injecting constructor
	 * @param application
	 * @param service
	 */
	public MarkdownHtmlConverter(Application application, Service<Result<Page>, Page> service) {
		this.application = application;
		this.wrapped = service;
	}

	/**
	 * Get page by namespace, key and optional revision
	 */
	@Override
	public Result<Page> get(String namespace, String key, Option<Long> revision) {
		// transparent: deligate to wrapped service
		return this.wrapped.get(namespace, key, revision);
	}

	/**
	 * Put page at namespace, key and optional revision
	 */
	@Override
	public Result<Page> put(Page page, String namespace, String key, Option<Long> revision) {
		// Convert markdown to html
		try {
			page.content = new Markdown4jProcessor().process(page.source);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		// Deligate to wrapped service
		return this.wrapped.put(page, namespace, key, revision);
	}
	
}
