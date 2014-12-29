package com.stys.platform.pages;

import play.libs.F;
import play.mvc.Content;

/**
 *
 */
public interface Result {

    public Content getContent();

    public Status getStatus();

    public enum Status {
        Ok, NotFound, Forbidden
    }

}
