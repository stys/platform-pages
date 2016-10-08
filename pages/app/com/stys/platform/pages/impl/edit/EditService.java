package com.stys.platform.pages.impl.edit;


import com.stys.platform.pages.api.Result;
import com.stys.platform.pages.impl.domain.*;
import play.twirl.api.Content;

import javax.inject.Inject;
import javax.inject.Named;

public class EditService implements ContentService {

    private ContentService delegate;

    @Inject
    public EditService(@Named("edit:delegate") ContentService delegate) {
        this.delegate = delegate;
    }

    @Override
    public Result<Content> get(Selector selector) {
        return delegate.get(selector);
    }

    @Override
    public Result<Content> put(Selector selector, Page data) {
        return this.delegate.put(selector, data);
    }
}