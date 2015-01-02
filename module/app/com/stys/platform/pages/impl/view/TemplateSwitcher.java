package com.stys.platform.pages.impl.view;

import java.util.Map;

import play.mvc.Content;

import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.Page;

/**
 * Simple template switcher
 */
public class TemplateSwitcher implements Template<Page> {

	// Registry of templates
	private Map<String, Template<Page>> templates;
	
	public TemplateSwitcher(Map<String, Template<Page>> templates) {
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
					return "text/plain";
				}

			};
		}

		// Render required template
		return template.render(page);
		
	}

}
