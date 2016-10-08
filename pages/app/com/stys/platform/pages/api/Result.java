package com.stys.platform.pages.api;


public interface Result<T> {

    public T getContent();

    public Status getStatus();

    public enum Status {
        Ok, BadRequest, NotFound, Unauthorized, Forbidden
    }

}
