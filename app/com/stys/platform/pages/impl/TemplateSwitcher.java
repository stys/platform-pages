package com.stys.platform.pages.impl;

import java.util.Map;

import play.mvc.Content;

import com.stys.platform.pages.Template;

/**
 * A facade for multiple templates
 */
public class TemplateSwitcher implements Template<SimplePage> {

	// Registry of templates
	private Map<String, Template<String>> templates;
	
	public TemplateSwitcher(Map<String, Template<String>> templates) {
		this.templates = templates;
	}
	
	@Override
	public Content render(final SimplePage page) {
	
		// Get a template
		Template<String> template = templates.get(page.template);
		
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
		return template.render(page.content);
		
	}

	
	
}
