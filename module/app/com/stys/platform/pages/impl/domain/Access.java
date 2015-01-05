package com.stys.platform.pages.impl.domain;

/**
 * Access options for page
 */
public enum Access {
	Private, 	// Can view: owner, administrator; Can edit: owner, administrator
	Internal,   // Can view: owner, moderator, administrator; Can edit: owner, moderator, administrator
	Protected,	// Can view: user; Can edit: owner, moderator, administrator
	Public, 	// Can view: any; Can edit: owner, moderator, administrator
	Open		// Can view: any; Can edit: any
}