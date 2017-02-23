package com.stys.platform.pages.markdown;

import org.markdown4j.Markdown4jProcessor;
import org.markdown4j.Plugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;

@Singleton
public class DefaultMarkdownProcessor implements MarkdownProcessor {

    private Markdown4jProcessor processor;

    @Inject
	public DefaultMarkdownProcessor(MarkdownPluginsProvider markdownPluginsProvider) {
        this.processor = new Markdown4jProcessor();

        List<Plugin> markdownPlugins = markdownPluginsProvider.plugins();
        this.processor = this.processor.registerPlugins(markdownPlugins.toArray(new Plugin[markdownPlugins.size()]));
	}


	/** Converts markdown to HTML before persisting */
	@Override
	public String process(String source) {
		// Convert markdown to html
		try {
			return this.processor.process(source);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}
