package com.stys.platform.pages;

import play.libs.F;
import play.mvc.Content;
import play.mvc.Result;

/**
 *
 */
public abstract class Pages {

    private Template<String> template;
    private Repository<String> repository;

    /**
     * Default constructor
     * @param template - template service
     * @param repository - repository service
     */
    public Pages(Template<String> template, Repository<String> repository) {
        this.template = template;
        this.repository = repository;
    }

    /**
     * Service implementation
     */
    public F.Option<Content> get(String namespace, String key, Long version) {

        // Get page content
        String content = repository.getOr(namespace, key, version, null);

        // Check for missing
        if (null == content) {
            return F.Option.None();
        }

        // Return rendered content
        return F.Option.Some(template.render(content));
    }

}
