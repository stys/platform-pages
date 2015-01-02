package com.stys.platform.pages.impl.utils;

import com.stys.platform.pages.Result;
import play.Application;

import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.Page;
import play.mvc.Content;

public class AccessUtils {

	private static final String VIEW_ACCESS_MANAGER_KEY = "com.stys.platform.pages.access.view";
	private static final String EDIT_ACCESS_MANAGER_KEY = "com.stys.platform.pages.access.edit";
	
	public static Service<Result<Content>, Page> getViewAccessManager(Application application) {
		
		String clazz = application.configuration().getString(VIEW_ACCESS_MANAGER_KEY);		
		try {
			@SuppressWarnings("unchecked")
			Service<Result<Content>, Page> manager = (Service<Result<Content>, Page>) application.classloader().loadClass(clazz).newInstance();
			return manager;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
	}
	
	public static Service<Result<Content>, Page> getEditAccessManager(Application application) {

		String clazz = application.configuration().getString(EDIT_ACCESS_MANAGER_KEY);
		try {
			@SuppressWarnings("unchecked")
			Service<Result<Content>, Page> manager = (Service<Result<Content>, Page>) application.classloader().loadClass(clazz).newInstance();
			return manager;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}
	
}
