package com.stys.platform.pages.impl.view;

import com.stys.platform.pages.api.Result;
import com.stys.platform.pages.api.Results;
import com.stys.platform.pages.impl.domain.*;

import javax.inject.Inject;
import javax.inject.Named;

public class ViewAccessManager extends Results implements PageService {

	private UserService	users;

	private PageService delegate;
	
	@Inject
	public ViewAccessManager(
        @Named("view:access:delegate") PageService delegate,
        UserService users
	) {
        this.delegate = delegate;
        this.users = users;
	}
	
	@Override
	public Result<Page> get(Selector selector) {
		
		// Default result
		Result<com.stys.platform.pages.impl.domain.Page> result = BadRequest(null);
		
		// Get page from delegate service
		Result<com.stys.platform.pages.impl.domain.Page> previous = this.delegate.get(selector);
		
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
	public Result<Page> put(Selector selector, Page page) {
		// Default return bad request: view service doesn't allow put!
		return BadRequest(null);		
	}

}
