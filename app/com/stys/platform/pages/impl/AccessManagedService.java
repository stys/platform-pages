package com.stys.platform.pages.impl;

import play.libs.F.Option;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;

public class AccessManagedService implements Service<ContentResult, Page> {

	private Service<ContentResult, Page> wrapped;
	
	private Service<ContentResult, Page> access;
	
	public AccessManagedService(Service<ContentResult, Page> service, Service<ContentResult, Page> access) {
		this.wrapped = service;
		this.access = access;
	}

	@Override
	public ContentResult get(String namespace, String key, Option<Long> revision) {
		
		// Check permissions
		ContentResult result = access.get(namespace, key, revision);
		
		// If something not ok - return the result
		if ( !result.getStatus().equals(Result.Status.Ok) ) {
			return result;
		}

		// Now use main service
		return wrapped.get(namespace, key, revision);
		
	}

	@Override
	public ContentResult put(Page page, String namespace, String key, Option<Long> revision) {
		
		// Check permissions
		ContentResult result = access.put(page, namespace, key, revision);
		
		// If something not ok - return the result
		if ( !result.getStatus().equals(Result.Status.Ok) ) {
			return result;
		}

		// Now use main service
		return wrapped.get(namespace, key, revision);
		
	}
	
}
