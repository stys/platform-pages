package com.stys.platform.pages.impl.domain;

public class Page {

	public String namespace;
	
	public String key;
	
	public String owner;
	
	public Access access;
	
	public State state;

	public String template;

	public Long revision;

	public String title;

	public String source;

	public String content;

	@Override
	public String toString() {
		return String.format("n: %s, k: %s, st: %s, t: %s, r: %d, so: %s, c: %s", 
				namespace, key, state, template, revision, source, content);
	}
	
}
