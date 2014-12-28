package com.stys.platform.pages.impl.edit;

import play.libs.F;
import play.libs.F.Option;
import play.mvc.Content;

import com.stys.platform.pages.Processor;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.BasicService;
import com.stys.platform.pages.impl.Page;

public class DefaultEditService implements Service<Page> {

	private BasicService<Page> wrapped;

	private Template<Page> editor;
	
	private Processor<Page> processor;
	
	public DefaultEditService(Template<Page> editor, Repository<Page> repository, Processor<Page> processor) {
		
		// keep reference to editor
		this.editor = editor;
		
		// create a basic pages service
		this.wrapped = new BasicService<>(editor, repository);
		
		// store converter
		this.processor = processor;
	
	}
	
	@Override
	public Option<Content> get(String namespace, String key, Option<Long> revision) {
	
		// Retrieve page for editting 
		Option<Content> content = wrapped.get(namespace, key, revision);
	
		// If page is empty - we are creating a new one
		if( content.isEmpty() ) {
			Page page = new Page();
			page.namespace = namespace;
			page.key = key;
			content = F.Some(editor.render(page));
		}
		
		// Done
		return content;
	}

	@Override
	public void put(Page page, String namespace, String key, Option<Long> revision) {
		
		// execute processor, then put
		page = this.processor.process(page);
		
		// deligate
		wrapped.put(page, namespace, key, revision);		
	
	}
	
}
