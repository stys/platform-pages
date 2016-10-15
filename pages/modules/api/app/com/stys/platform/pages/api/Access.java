package com.stys.platform.pages.api;

/** Access options for page */
public enum Access {

    /* Can create: moderator, can view: owner, can edit: owner */
	Private,

    /* Can create: moderator, can view: moderator, can edit: moderator */
    Internal,

    /* Can create: moderator, can view: user, can edit: moderator */
	Protected,

    /* Can create: moderator, can view: anonymous, can edit: moderator */
	Public,

    /* Can create: user, can view: anonymous, can edit: user */
	Open

}