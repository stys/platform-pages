package com.stys.platform.pages.impl;

/**
 * Resolvable name for implementations of {@link SimplePagesPlugin}
 */
public abstract class SimplePagesPlugin extends com.stys.platform.pages.Plugin<SimplePage> {

	public abstract SimplePages getPagesService();
	
}
