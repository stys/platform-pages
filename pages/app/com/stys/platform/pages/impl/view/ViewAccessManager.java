package com.stys.platform.pages.impl.view;

import java.lang.reflect.Constructor;
import java.nio.channels.SelectableChannel;

import play.Application;
import play.Logger;
import play.libs.F.Option;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Results;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.*;

public class ViewAccessManager<S> extends Results implements Service<Result<Page>, S, Page> {

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
	private Service<Result<Page>, S, Page> delegate;
	
	/**
	 * Constructor
	 * @param application - injected instance of application
	 * @param delegate - injected instance of delegate service
	 */
	public ViewAccessManager(Application application, Service<Result<Page>, S, Page> delegate) {
		
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
	public Result<Page> get(S selector) {
		
		// Default result
		Result<Page> result = BadRequest(null);
		
		// Get page from delegate service
		Result<Page> previous = this.delegate.get(selector);
		
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
		boolean isDraft = State.Draft.equals(state);
		boolean isPublished = State.Published.equals(state);
		boolean isClosed = State.Closed.equals(state);
		boolean isDeleted = State.Deleted.equals(state);
		
		boolean isPrivate = Access.Private.equals(access);
		@SuppressWarnings("unused")
		boolean isInternal = Access.Internal.equals(access);;
		boolean isProtected = Access.Protected.equals(access);;
		boolean isPublic = Access.Public.equals(access);;
		boolean isOpen = Access.Open.equals(access);;
		
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
	public Result<Page> put(S selector, Page page) {
					
		// Default return bad request: view service doesn't allow put!
		return BadRequest(null);		
	}

}
