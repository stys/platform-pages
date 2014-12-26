package com.stys.platform.pages.impl;

import com.stys.platform.pages.Repository;
import play.Logger;
import play.libs.F;

/**
 * Wrapper over base repository. Converts from markdown source to
 * html content. Sanitizes produced html.
 */
public class ConvertingPagesRepository implements Repository<Page> {

    private Repository<Page> repository;

    public ConvertingPagesRepository(Repository<Page> repository) {
        this.repository = repository;
    }

    @Override
    public void put(Page page, String namespace, String key, F.Option<Long> revision) {

        // convert source to content (markdown to html)
        // TODO:
        page.content = page.source;

        // pass to parent method
        this.repository.put(page, namespace, key, revision);

    }

    @Override
    public F.Option<Page> get(String namespace, String key, F.Option<Long> revision){
        return this.repository.get(namespace, key, revision);
    }

}
