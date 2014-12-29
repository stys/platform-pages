package com.stys.platform.pages.impl;

import com.stys.platform.pages.Result;
import play.mvc.Content;

/**
 * Created by stys on 29.12.14.
 */
public class ResultUtils {

    public static Result Forbidden(Content content) {
        return new 
    }

    public static Result NotFound(Content content) {
        return new R(content, Result.Status.NotFound);
    }

    private static class R implements Result {

        private Content content;
        private Result.Status status;

        public R(Content content, Result.Status status) {
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
