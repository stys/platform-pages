package com.stys.platform.pages;

import play.mvc.Content;

/**
 * Template service. Page content may be rendered by any
 * template system, such as Play's templates, or any other
 * template engine. 
 */
public interface Template<T> {

    public Content render(T content);

}
