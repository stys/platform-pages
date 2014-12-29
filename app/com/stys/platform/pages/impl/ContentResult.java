package com.stys.platform.pages.impl;

import play.mvc.Content;
import play.mvc.Results;

import com.stys.platform.pages.Result;

public abstract class ContentResult implements Result<Content> {

	public play.mvc.Result toPlayMvcResult() {
			
		switch(this.getStatus()) {
			case Ok:
				return Results.ok(this.getContent());
			case NotFound:
				return Results.notFound(this.getContent());
			case Unauthorized:
				return Results.unauthorized(this.getContent());
			case Forbidden:
				return Results.forbidden(this.getContent());
			default:
				throw new IllegalStateException(String.format("Unexpected status: %s", this.getStatus()));
		}
	}
	
}