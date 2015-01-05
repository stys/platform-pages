package com.stys.platform.pages.impl.edit;

import java.lang.reflect.Constructor;

import play.Application;
import play.Logger;
import play.libs.F.Option;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Results;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.*;

public class EditAccessManager extends Results implements Service<Result<Page>, Page> {

	/**
	 * User service configuration key
	 */
	private static final String USER_SERVICE_KEY = "com.stys.platform.pages.edit.user.service";
	
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
	public EditAccessManager(Application application, Service<Result<Page>, Page> delegate) {
		
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
		
		// If page not found - creating a new one
		boolean isCreating = previous.getStatus().equals(Result.Status.NotFound);
		
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
		@SuppressWarnings("unused")
		boolean isProtected = access.equals(Access.Protected);
		@SuppressWarnings("unused")
		boolean isPublic = access.equals(Access.Public);
		boolean isOpen = access.equals(Access.Open);
		
		// No user present
		if(! isUserPresent) {
			// Anonymous users can not edit
			return Unauthorized(null);
		}
		
		// Simple user, not owner of the page
		if( (!isOwner) && isUser ) {
			// Registered users can edit published open pages and create new open pages
			if( (!isCreating) && isOpen ) return previous;
			else if (isCreating && isOpen ) return Ok(previous.getContent());
			else result = Forbidden(null);
		}
		
		// Moderator, not owner
		if( (!isOwner) && isModerator) {
			// Moderators can edit published and closed pages
			if ( (! isCreating) && (isPublished || isClosed) && (! isPrivate) ) return previous;
			else if (isCreating) return Ok(previous.getContent());
			else result = Forbidden(null);
		}
		
		// Administrator, not owner
		if( (!isOwner) && isAdministrator ) {
			// Administrators can edit any page
			return Ok(previous.getContent());
		}
		
		// Owner
		if (isOwner) {
			// Owner can not see deleted pages
			if (isDeleted) result = NotFound(null);
			else return Ok(previous.getContent());
		}
		
		// Return final result
		return result;
	}

	@Override
	public Result<Page> put(Page page, String namespace, String key, Option<Long> revision) {
		
		// Default result
		Result<Page> result = BadRequest(null);
		
		// Get page from delegate service
		Result<Page> previous = this.delegate.get(namespace, key, revision);
		
		// If page not found - creating a new one
		boolean isCreating = previous.getStatus().equals(Result.Status.NotFound);
		
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
		@SuppressWarnings("unused")
		boolean isClosed = State.Closed.equals(state);
		@SuppressWarnings("unused")
		boolean isDeleted = State.Deleted.equals(state);
		
		boolean isPrivate = Access.Private.equals(access);
		@SuppressWarnings("unused")
		boolean isInternal = Access.Internal.equals(access);
		@SuppressWarnings("unused")
		boolean isProtected = Access.Protected.equals(access);
		@SuppressWarnings("unused")
		boolean isPublic = Access.Public.equals(access);
		boolean isOpen = Access.Open.equals(access);
		
		// No user present
		if( !isUserPresent ) { 
			// Anonymous users can not edit
			return Unauthorized(null);
		}
		
		// If creating - set owner of the page
		if( isCreating ) {
			page.owner = user.getID();
		}
				
		// Simple user, not owner of the page
		if( (!isOwner) && isUser ) {
			// Registered users can edit published open pages and create new open pages
			if( (isPublished && isOpen) || (isCreating && isOpen) ) {
				// Make sure user did not change access level
				if (page.access.equals(Access.Open)) return this.delegate.put(page, namespace, key, revision);
				else result = Forbidden(null);
			} 
			else result = Forbidden(null);
		}
		
		// Moderator, not owner of the page
		if( (!isOwner) && isModerator ) {
			// Can edit any page, except private pages
			if ( !isPrivate ) {
				// Make sure moderator did not set page to private
				if ( page.access.equals(Access.Private) ) result = Forbidden(null);
				else return this.delegate.put(page, namespace, key, revision);
			} else if (isCreating) {
				return this.delegate.put(page, namespace, key, revision);
			}
		}
		
		// Administrator
		if (isAdministrator) {
			// Administrator can edit always
			return this.delegate.put(page, namespace, key, revision);
		}
		
		// Owner
		if (isOwner) {
			// Owner can edit
			return this.delegate.put(page, namespace, key, revision);
		}
		
		// Return final result
		return result;
	}

}
