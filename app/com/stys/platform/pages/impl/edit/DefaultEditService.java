package com.stys.platform.pages.impl.edit;


import play.Logger;
import play.libs.F.Option;
import play.mvc.Content;

import com.stys.platform.pages.Processor;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.Page;
import com.stys.platform.pages.impl.BasicService;

public class DefaultEditService implements Service<Page> {

	private BasicService<Page> wrapped;
	private Processor<Page> processor;
	
	public DefaultEditService(Template<Page> editor, Repository<Page> repository, Processor<Page> processor) {
		
		// create a basic pages service
		this.wrapped = new BasicService<>(editor, repository);
		
		// store converter
		this.processor = processor;
	
	}
	
	
	@Override
	public Option<Content> get(String namespace, String key, Option<Long> revision) {
		// deligate
		return wrapped.get(namespace, key, revision);
	}

	@Override
	public void put(Page page, String namespace, String key, Option<Long> revision) {
		// execute processor, then put
		page = this.processor.process(page);
		Logger.info(page.toString());
		// deligate
		wrapped.put(page, namespace, key, revision);		
	}
	
	
	
}
