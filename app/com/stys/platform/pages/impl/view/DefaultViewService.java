package com.stys.platform.pages.impl.view;

import play.libs.F.Option;
import play.mvc.Content;

import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.Page;
import com.stys.platform.pages.impl.BasicService;


public class DefaultViewService implements Service<Page> {

	private Service<Page> wrapped;
	
	public DefaultViewService(Template<Page> template, Repository<Page> repository) {
		this.wrapped = new BasicService<>(template, repository);
	}

	@Override
	public Option<Content> get(String namespace, String key, Option<Long> revision) {
		return this.wrapped.get(namespace, key, revision);
	}

	@Override
	public void put(Page page, String namespace, String key, Option<Long> revision) {
		this.wrapped.put(page, namespace, key, revision);		
	}
	
}
