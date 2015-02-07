package com.stys.platform.pages.impl.domain;

/**
 * Access options for page
 */
public enum Access {
	Private, // Can create: moderator, can view: owner, can edit: owner
	Internal, // Can create: moderator, can view: moderator, can edit: moderator
	Protected,	// Can create: moderator, can view: user, can edit: moderator
	Public, // Can create: moderator, can view: anonymous, can edit: moderator 
	Open // Can create: user, can view: anonymous, can edit: user
}