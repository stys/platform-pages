package com.stys.platform.pages;

/**
 * Pages service plugin
 */
public abstract class Plugin<T> extends play.Plugin {

	/**
	 * Retrieve pages service
	 * @return
	 */
    public abstract Service<T> getPagesService();

}
