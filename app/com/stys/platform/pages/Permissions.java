package com.stys.platform.pages;

/**
 *
 */
public interface Permissions {

    public boolean check(String namespace, String key, String action);

}
