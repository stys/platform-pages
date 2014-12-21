package com.stys.platform.pages.impl;

import java.util.Map;

import play.mvc.Content;

import com.stys.platform.pages.Template;

public class EditorTemplate implements Template<Page> {

	// Editor template
	private Template<PageEdit> editor;
	
	// Registry of view templates
	private Map<String, Template<Page>> templates;
	
	// Constructor 
	public EditorTemplate(Template<PageEdit> editor, Map<String, Template<Page>> templates) {
		this.editor = editor;
		this.templates = templates;
	}

	@Override
	public Content render(Page page) {
		
		PageEdit edit = new PageEdit();
		edit.namespace = page.namespace;
		edit.key = page.key;
		edit.title = page.title;
		edit.content = page.content;
		edit.template = page.template;
		edit.templates = templates.keySet();
		
		return editor.render(edit);
		
	}
	
	
	
	
}
