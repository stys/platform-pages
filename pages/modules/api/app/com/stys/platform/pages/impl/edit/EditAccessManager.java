package com.stys.platform.pages.impl.edit;

import com.stys.platform.pages.api.*;

import javax.inject.Inject;
import javax.inject.Named;

public class EditAccessManager extends Results implements PageService {

	private UserService	users;

	private PageService delegate;

	@Inject
	public EditAccessManager(
        @Named("edit:access:delegate") PageService delegate,
        UserService users
    ) {
		this.delegate = delegate;
        this.users = users;
	}
	
	@Override
	public Result<Page> get(Selector selector) {
		
		// Default result
		Result<Page> result = BadRequest(null);
		
		// Get page from delegate service
		Result<Page> previous = this.delegate.get(selector);
		
		// If page not found - creating a new one
		boolean isCreating = previous.getStatus().equals(Result.Status.NotFound);
		
		// Get access level and status
		Access access = previous.getPayload().access;
		State state = previous.getPayload().state;
		
		// Get owner
		String owner = previous.getPayload().owner;
		
		// Get user from user service
		User user = users.getUser();

		// Conditions
		boolean isUserPresent = (null != user);
		boolean isOwner = isUserPresent && user.getID().equals(owner);
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
		boolean isInternal = Access.Internal.equals(access);
		@SuppressWarnings("unused")
		boolean isProtected = Access.Protected.equals(access);
		@SuppressWarnings("unused")
		boolean isPublic = Access.Public.equals(access);
		boolean isOpen = Access.Open.equals(access);
		
		// If creating - set default access level
		if ( isCreating ) {
			previous.getPayload().state = State.Draft;
			previous.getPayload().access = Access.Protected;
		}
		
		// No user present
		if(! isUserPresent) {
			// Anonymous users can not edit
			return Unauthorized(null);
		}
		
		// Simple user, not owner of the page
		if( (!isOwner) && isUser ) {
			// Registered users can edit published open pages and create new open pages
			if( (!isCreating) && isOpen ) return previous;
			else if (isCreating && isOpen ) return Ok(previous.getPayload());
			else result = Forbidden(null);
		}
		
		// Moderator, not owner
		if( (!isOwner) && isModerator) {
			// Moderators can edit published and closed pages
			if ( (! isCreating) && (isPublished || isClosed) && (! isPrivate) ) return previous;
			else if (isCreating) return Ok(previous.getPayload());
			else result = Forbidden(null);
		}
		
		// Administrator, not owner
		if( (!isOwner) && isAdministrator ) {
			// Administrators can edit any page
			return Ok(previous.getPayload());
		}
		
		// Owner
		if (isOwner) {
			// Owner can not see deleted pages
			if (isDeleted) result = NotFound(null);
			else return Ok(previous.getPayload());
		}
		
		// Return final result
		return result;
	}

	@Override
	public Result<Page> put(Selector selector, Page page) {
		
		// Default result
		Result<Page> result = BadRequest(null);
		
		// Get page from delegate service
		Result<Page> previous = this.delegate.get(selector);
		
		// If page not found - creating a new one
		boolean isCreating = previous.getStatus().equals(Result.Status.NotFound);
		
		// Get access level and status
		Access access = previous.getPayload().access;
		State state = previous.getPayload().state;
		
		// Get owner
		String owner = previous.getPayload().owner;
		
		// Get user from user service
		User user = users.getUser();

		// Conditions
		boolean isUserPresent = (null != user);
		boolean isOwner = isUserPresent && user.getID().equals(owner);
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
				if (page.access.equals(Access.Open)) return this.delegate.put(selector, page);
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
				else return this.delegate.put(selector, page);
			} else if (isCreating) {
				return this.delegate.put(selector, page);
			}
		}
		
		// Administrator
		if (isAdministrator) {
			// Administrator can edit always
			return this.delegate.put(selector, page);
		}
		
		// Owner
		if (isOwner) {
			// Owner can edit
			return this.delegate.put(selector, page);
		}
		
		// Return final result
		return result;
	}

}
