package com.stys.platform.pages;

/**
 * Pages service plugin used to wrap a page service. Wrapping 
 * service in plugin allows to have many page services in one
 * Play application and lookup them from Play plugins registry 
 * by plugin class name.
 */
public abstract class Plugin<R, T> extends play.Plugin {

	/**
	 * Returns an instance of page service
	 * @return
	 */
    public abstract Service<R, T> getPageService();

}
