package com.stys.platform.pages.markdown;

import com.stys.platform.pages.api.Result;
import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Selector;
import com.stys.platform.pages.api.PageService;
import org.markdown4j.Markdown4jProcessor;
import org.markdown4j.Plugin;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;


/** Markdown processor for pages pipeline. Uses markdown4j internally */
public class MarkdownProcessor implements PageService {

	private PageService delegate;

    private Markdown4jProcessor processor;

    @Inject
	public MarkdownProcessor(
        @Named("processor:delegate") PageService delegate,
        MarkdownPluginsProvider markdownPluginsProvider
    ) {
		this.delegate = delegate;
        this.processor = new Markdown4jProcessor();

        List<Plugin> markdownPlugins = markdownPluginsProvider.plugins();
        this.processor = this.processor.registerPlugins(markdownPlugins.toArray(new Plugin[markdownPlugins.size()]));
	}

	@Override
	public Result<Page> get(Selector selector) {
		return this.delegate.get(selector);
	}

	/** Converts markdown to HTML before persisting */
	@Override
	public Result<Page> put(Selector selector, Page page) {
		// Convert markdown to html
		try {
			page.content = this.processor.process(page.source);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		// Delegate
		return this.delegate.put(selector, page);
	}
	
}
