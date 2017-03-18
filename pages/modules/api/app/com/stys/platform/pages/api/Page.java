package com.stys.platform.pages.api;

import org.joda.time.DateTime;

import java.time.LocalDate;

public class Page {

    public String namespace = "";

    public String key = "";

    public String owner;

    public Access access = Access.Private;

    public State state = State.Draft;

    public String template;

    public Long revision;

    public String title;

    public String source;

    public String content;

    public String summary;

    public String thumb;

    public String description;

    public String keywords;
    
    public String category;

    public DateTime created;

    public DateTime published;
    
    public DateTime edited;

    @Override
    public String toString() {
        return String.format("namespace: %s, key: %s, owner: %s, access: %s, state: %s, template: %s, revision: %d, src: %s, content: %s, summary: %s, thumb: %s, description: %s, keywords: %s, category: %s",
                namespace, key, owner, access.name(), state.name(), template, revision, source, content, summary, thumb, description, keywords, category);
    }

}
