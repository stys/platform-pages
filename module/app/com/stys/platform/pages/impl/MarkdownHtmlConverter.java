package com.stys.platform.pages.impl;

import java.io.IOException;

import com.stys.platform.pages.Processor;

import org.markdown4j.Markdown4jProcessor;

public class MarkdownHtmlConverter implements Processor<Page> {
	
	@Override
	public Page process(Page page) {

		try {
			page.content = new Markdown4jProcessor().process(page.source);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		
		return page;
	}
	
}
