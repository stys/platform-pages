package com.stys.platform.pages.impl.utils;

import com.stys.platform.pages.Result;

/**
 * Result helpers
 */
public class ResultUtils<T> {

	public Result<T> Ok(T content) {
        return new SimpleResult(content, Result.Status.Ok);
	}
	
	public Result<T> Unauthorized(T content) {
		return new SimpleResult(content, Result.Status.Unauthorized);
	}
	
    public Result<T> Forbidden(T content) {
        return new SimpleResult(content, Result.Status.Forbidden);
    }

    public Result<T> NotFound(T content) {
        return new SimpleResult(content, Result.Status.NotFound);
    }

    private class SimpleResult implements Result<T> {

        private T content;
        private Result.Status status;

        public SimpleResult(T content, Result.Status status) {
            this.content = content;
            this.status = status;
        }

        @Override
        public T getContent() {
            return this.content;
        }

        @Override
        public Status getStatus() {
            return this.status;
        }
    }

}
