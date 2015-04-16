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

    public String summary;

    public String thumb;

    public String description;

    public String keywords;

    @Override
    public String toString() {
        return String.format("namespace: %s, key: %s, owner: %s, access: %s, state: %s, template: %s, revision: %d, src: %s, content: %s, summary: %s, thumb: %s, description: %s, keywords: %s",
                namespace, key, owner, access.name(), state.name(), template, revision, source, content, summary, thumb, description, keywords);
    }

}
