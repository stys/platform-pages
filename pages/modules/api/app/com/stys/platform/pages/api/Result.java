package com.stys.platform.pages.api;

import play.mvc.Results;
import play.twirl.api.Content;

import java.util.function.Function;

import static com.stys.platform.pages.api.Result.Status.*;

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

    public play.mvc.Result toPlayMvcResult() {
        switch (this.getStatus()) {
            case Ok:
                return Results.ok((Content) this.getPayload());
            case BadRequest:
                return Results.badRequest((Content) this.getPayload());
            case NotFound:
                return Results.notFound((Content) this.getPayload());
            case Unauthorized:
                return Results.unauthorized((Content) this.getPayload());
            case Forbidden:
                return Results.forbidden((Content) this.getPayload());
            default:
                throw new IllegalStateException(String.format("Unexpected status: %s", this.getStatus()));
        }
    }

}
