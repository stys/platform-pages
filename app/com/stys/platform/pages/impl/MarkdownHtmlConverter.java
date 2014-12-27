package com.stys.platform.pages.impl;

import com.stys.platform.pages.Processor;

public class MarkdownHtmlConverter implements Processor<Page> {

	@Override
	public Page process(Page page) {
		// FIXME:
		page.content = page.source;
		return page;
	}
	
}
