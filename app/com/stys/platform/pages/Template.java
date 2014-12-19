package com.stys.platform.pages;

import play.mvc.Content;

/**
 *
 */
public interface Template<T> {

    public Content render(T content);

}
