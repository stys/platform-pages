package com.stys.platform.pages.impl.markdown4j;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.Selector;
import com.stys.platform.pages.impl.domain.Page;
import play.Application;

/**
 * Default implementation of markdown processor for namespace-key-revision selector
 */
public class DefaultMarkdownProcessor extends MarkdownProcessor<Result<Page>, Selector> {
    public DefaultMarkdownProcessor(Application application, Service<Result<Page>, Selector, Page> delegate) {
        super(application, delegate);
    }
}

