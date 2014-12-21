package com.stys.platform.pages.impl;

import com.stys.platform.pages.Pages;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Template;


public class PagesService extends Pages<Page> {

	public PagesService(Template<Page> template, Repository<Page> repository) {
		super(template, repository);
	}
	
}
