package com.stys.platform.pages;

import play.mvc.Controller;


/**
 * Result helpers
 */
public class Results extends Controller {

	public <T> Result<T> Ok(T content) {
        return new SimpleResult<T>(content, Result.Status.Ok);
	}
	
	public <T> Result<T> BadRequest(T content) {
		return new SimpleResult<T>(content, Result.Status.BadRequest);
	}
	
	public <T> Result<T> Unauthorized(T content) {
		return new SimpleResult<T>(content, Result.Status.Unauthorized);
	}
	
    public <T> Result<T> Forbidden(T content) {
        return new SimpleResult<T>(content, Result.Status.Forbidden);
    }

    public <T> Result<T> NotFound(T content) {
        return new SimpleResult<T>(content, Result.Status.NotFound);
    }
    
    public <U, T> Result<U> map(Result<T> previous, U content) {
    	return new SimpleResult<U>(content, previous.getStatus());
    }
    
    private class SimpleResult<U> implements Result<U> {

        private U content;
        private Result.Status status;

        public SimpleResult(U content, Result.Status status) {
            this.content = content;
            this.status = status;
        }

        @Override
        public U getContent() {
            return this.content;
        }

        @Override
        public Status getStatus() {
            return this.status;
        }
    }

}
