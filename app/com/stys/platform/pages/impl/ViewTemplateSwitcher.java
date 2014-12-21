package com.stys.platform.pages.impl;

import java.util.Map;

import play.mvc.Content;

import com.stys.platform.pages.Template;

/**
 * A facade for multiple templates
 */
public class ViewTemplateSwitcher implements Template<Page> {

	// Registry of templates
	private Map<String, Template<Page>> templates;
	
	public ViewTemplateSwitcher(Map<String, Template<Page>> templates) {
		this.templates = templates;
	}
	
	@Override
	public Content render(final Page page) {
	
		// Get a template
		Template<Page> template = templates.get(page.template);
		
		// Check template
		if (null == template) {
			
			// Result for missing template
			return new Content() {

				@Override
				public String body() {
					return page.content;
				}

				@Override
				public String contentType() {
					return "application/text";
				}
				
			};
		}
		
		// Render required template
		return template.render(page);
		
	}

	
	
}
