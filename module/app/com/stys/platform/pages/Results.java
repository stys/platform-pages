package com.stys.platform.pages;


/**
 * Result helpers
 */
public class Results<T> {

	public Result<T> Ok(T content) {
        return new SimpleResult<T>(content, Result.Status.Ok);
	}
	
	public Result<T> Redirect(T content) {
		return new SimpleResult<T>(content, Result.Status.Redirect);
	}
	
	public Result<T> BadRequest(T content) {
		return new SimpleResult<T>(content, Result.Status.BadRequest);
	}
	
	public Result<T> Unauthorized(T content) {
		return new SimpleResult<T>(content, Result.Status.Unauthorized);
	}
	
    public Result<T> Forbidden(T content) {
        return new SimpleResult<T>(content, Result.Status.Forbidden);
    }

    public Result<T> NotFound(T content) {
        return new SimpleResult<T>(content, Result.Status.NotFound);
    }
    
    public <U> Result<U> map(Result<T> previous, U content) {
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
