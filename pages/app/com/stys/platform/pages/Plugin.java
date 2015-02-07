package com.stys.platform.pages;

/**
 * Pages service plugin
 */
public abstract class Plugin<R, T> extends play.Plugin {

	/**
	 * Retrieve show service
	 * @return
	 */
    public abstract Service<R, T> getPagesService();

}
