package com.stys.platform.pages.impl.domain;

/**
 * State opotions for page
 */
public enum State {
	Draft, 		// Can view: owner, administrator; Can edit: owner, administrator
	Published, 	// Can view: any; Can edit: any
	Closed, 	// Can view: any; Can edit: owner, moderator, administrator
	Deleted		// Can view: moderator, administrator; Can edit: moderator, administrator
}