package com.stys.platform.pages.api;

import java.util.function.Function;

public class Result<T> {

    public enum Status {
        Ok, BadRequest, NotFound, Unauthorized, Forbidden
    }

    private Status status;

    private T payload;

    private Result(Status status, T payload) {
        this.status = status;
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public Status getStatus() {
        return status;
    }

    public static <T> Result<T> of(Status status, T content) {
        return new Result<>(status, content);
    }

    public <U> Result<U> map(Function<Result<T>, U> transform) {
        return new Result<>(this.status, transform.apply(this));
    }

}
