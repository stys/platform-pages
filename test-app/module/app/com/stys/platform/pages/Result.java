package com.stys.platform.pages;

/**
 * Generic result type
 */
public interface Result<T> {

    public T getContent();

    public Status getStatus();

    public enum Status {
        Ok, Redirect, BadRequest, NotFound, Unauthorized, Forbidden
    }

}
