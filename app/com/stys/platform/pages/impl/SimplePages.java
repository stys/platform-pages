package com.stys.platform.pages.impl;

import com.stys.platform.pages.Pages;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Template;


public class SimplePages extends Pages<SimplePage> {

	public SimplePages(Template<SimplePage> template, Repository<SimplePage> repository) {
		super(template, repository);
	}
	
}
