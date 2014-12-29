package com.stys.platform.pages.utils;

import play.mvc.Content;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.impl.ContentResult;

/**
 * Created by stys on 29.12.14.
 */
public class ResultUtils {

	public static ContentResult Ok(Content content) {
		return new SimpleResult(content, Result.Status.Ok);
	}
	
	public static ContentResult Unauthorized(Content content) {
		return new SimpleResult(content, Result.Status.Unauthorized);
	}
	
    public static ContentResult Forbidden(Content content) {
        return new SimpleResult(content, Result.Status.Forbidden);
    }

    public static ContentResult NotFound(Content content) {
        return new SimpleResult(content, Result.Status.NotFound);
    }

    public static class SimpleResult extends ContentResult {

        private Content content;
        private Result.Status status;

        public SimpleResult(Content content, Result.Status status) {
            this.content = content;
            this.status = status;
        }

        @Override
        public Content getContent() {
            return this.content;
        }

        @Override
        public Status getStatus() {
            return this.status;
        }
    }

}
