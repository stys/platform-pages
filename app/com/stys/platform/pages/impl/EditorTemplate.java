package com.stys.platform.pages.impl;

import java.util.Map;

import play.mvc.Content;

import com.stys.platform.pages.Template;

/**
 * Wrapper over template, which ...
 */
public class EditorTemplate implements Template<Page> {

	// Editor template
	private Template<Page> editor;
	private Map<String, Template<Page>> templates;
	
	// Constructor 
	public EditorTemplate(Template<Page> editor) {

		// cache editor
		this.editor = editor;


	}

	@Override
	public Content render(Page page) {

		return editor.render(page);

	}
	
}
