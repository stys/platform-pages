package com.stys.platform.pages.api;

import play.twirl.api.Content;

public interface Template {

	public Content render(Page page);
	
}
