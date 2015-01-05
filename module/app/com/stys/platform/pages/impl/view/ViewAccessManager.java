package com.stys.platform.pages.impl.view;

import java.lang.reflect.Constructor;

import play.Application;
import play.Logger;
import play.libs.F.Option;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Results;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.*;

public class ViewAccessManager extends Results implements Service<Result<Page>, Page> {

	/**
	 * User service configuration key
	 */
	private static final String USER_SERVICE_KEY = "com.stys.platform.pages.view.user.service";
	
	/**
	 * Intance of user service
	 */
	private UserService	users;
	
	/**
	 * Instance of application
	 */
	private Application application;
	
	/**
	 * Instance of deligate service
	 */
	private Service<Result<Page>, Page> delegate;
	
	/**
	 * Constructor
	 * @param application - injected instance of application
	 * @param delegate - injected instance of delegate service
	 */
	public ViewAccessManager(Application application, Service<Result<Page>, Page> delegate) {
		
		// Store references
		this.application = application;
		this.delegate = delegate;
		
		// Load user service
		try {
			// Get name of user service class
			String name = this.application.configuration().getString(USER_SERVICE_KEY);
			// Write to log
			Logger.debug(String.format("Picked %s", name));
			// Load class
			Class<?> clazz = this.application.classloader().loadClass(name);
			// Get constructor
			Constructor<?> constructor = clazz.getConstructor(Application.class);
			// Create an instance
			this.users = (UserService) constructor.newInstance(this.application);
		} catch (Exception ex) {
			// Can not continue without user service
			throw new RuntimeException("Can not load user service", ex);
		}
		
	}
	
	@Override
	public Result<Page> get(String namespace, String key, Option<Long> revision) {
		
		// Default result
		Result<Page> result = BadRequest(null);
		
		// Get page from delegate service
		Result<Page> previous = this.delegate.get(namespace, key, revision);
		
		// If page not found - continue 
		if( previous.getStatus().equals(Result.Status.NotFound) ) {
			return previous;
		}
		
		// Get access level and status
		Access access = previous.getContent().access;
		State state = previous.getContent().state;
		
		// Get owner
		String owner = previous.getContent().owner;
		
		// Get user from user service
		User user = users.getUser();

		// Conditions
		boolean isUserPresent = (null != user);
		boolean isOwner = isUserPresent ? user.getID().equals(owner) : false;
		boolean isUser = isUserPresent && user.getRoles().contains(Role.User);
		boolean isModerator = isUserPresent && user.getRoles().contains(Role.Moderator);
		boolean isAdministrator = isUserPresent && user.getRoles().contains(Role.Administrator);
			
		@SuppressWarnings("unused")
		boolean isDraft = state.equals(State.Draft);
		boolean isPublished = state.equals(State.Published);
		boolean isClosed = state.equals(State.Closed);
		boolean isDeleted = state.equals(State.Deleted);
		
		boolean isPrivate = access.equals(Access.Private);
		@SuppressWarnings("unused")
		boolean isInternal = access.equals(Access.Internal);
		boolean isProtected = access.equals(Access.Protected);
		boolean isPublic = access.equals(Access.Public);
		boolean isOpen = access.equals(Access.Open);
		
		// No user present
		if( ! isUserPresent ) {
			// Can view if page is `published` or `closed` and access is `public` or `open`
			if( (isPublished || isClosed) && (isPublic || isOpen) ) return previous;
			else result = Unauthorized(null); 		
		}
		
		// Simple user, but not the owner of the page
		if( (! isOwner) && isUser ) {
			// Can view if page is `published` or `closed` and access is `protected`, `public` or `open`
			if( (isPublished || isClosed) && (isProtected || isPublic || isOpen) ) return previous;
			else result = Forbidden(null);
		}
		
		// Moderator, but nor owner of the page
		if( (! isOwner) && isModerator ) {
			// Can view any, except deleted and private
			if (isPrivate) result = Forbidden(null);
			else if (isDeleted) result = NotFound(null);
			else return previous;
		}
		
		// Owner of the page
		if ( isOwner ) {
			// Can view any, except deleted
			if (isDeleted) result = NotFound(null);
			else return previous;
		}
		
		// Administrator
		if ( isAdministrator ) {
			// Can view any
			return previous;
		}
		
		// Return final result
		return result;
	}

	@Override
	public Result<Page> put(Page page, String namespace, String key, Option<Long> revision) {
					
		// Default return bad request: view service doesn't allow put!
		return BadRequest(null);		
	}

}
