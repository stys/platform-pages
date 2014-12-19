package com.stys.platform.pages;

/**
 *
 */
public interface Repository<T> {

    public void put(String namespace, String name, Long version, T page);

    public T getOr(String namespace, String name, Long version, T default_);

}
