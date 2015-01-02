package com.stys.platform.pages.impl;

import play.libs.F.Option;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;

/**
 * Generic access manager service
 * @param <R> - type of service result
 * @param <T> - type of page data transfer object
 */
public class AccessManagedService<R, T> implements Service<Result<R>, T> {

	private Service<Result<R>, T> wrapped;
	
	private Service<Result<R>, T> access;
	
	public AccessManagedService(Service<Result<R>, T> service, Service<Result<R>, T> access) {
		this.wrapped = service;
		this.access = access;
	}

	@Override
	public Result<R> get(String namespace, String key, Option<Long> revision) {
		
		// Check permissions
		Result<R> result = access.get(namespace, key, revision);
		
		// If something not ok - return the result
		if ( !result.getStatus().equals(Result.Status.Ok) ) {
			return result;
		}

		// Now use main service
		return wrapped.get(namespace, key, revision);
		
	}

	@Override
	public Result<R> put(T page, String namespace, String key, Option<Long> revision) {
		
		// Check permissions
		Result<R> result = access.get(namespace, key, revision);
		
		// If something not ok - return the result
		if ( !result.getStatus().equals(Result.Status.Ok) ) {
			return result;
		}

		// Now use main service
		return wrapped.put(page, namespace, key, revision);
		
	}
	
}
