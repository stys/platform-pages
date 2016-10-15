package com.stys.platform.pages.impl.view;


import com.stys.platform.pages.api.Result;
import com.stys.platform.pages.api.ContentService;
import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Selector;
import play.twirl.api.Content;

import javax.inject.Inject;
import javax.inject.Named;

public class ViewService implements ContentService {

    private ContentService delegate;

    @Inject
    public ViewService(@Named("view:delegate") ContentService delegate) {
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
