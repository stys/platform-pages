package com.stys.platform.pages.impl.markdown4j;

import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.Page;
import org.markdown4j.Markdown4jProcessor;
import org.markdown4j.Plugin;
import play.Application;
import play.Configuration;
import play.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Markdown processor for pages pipeline. Uses markdown4j internally.
 * Allows to read markdown4j plugins from Play configuration.
 */
public class MarkdownProcessor<R, S> implements Service<R, S, Page> {
	
    /** Configuration key to map of markdown4j plugins */
    private static String MARKDOWN4J_PLUGINS_CONFIGURATION_KEY = "com.stys.platform.pages.markdown4j.plugins";
    
	/** Instance of delegate service */
	private Service<R, S, Page> delegate;

	/** Instance of application */
	@SuppressWarnings("unused")
	private Application application;

    /** Markdown processor */
    private Markdown4jProcessor processor;
    
	/**
	 * Constructs new instance and registers markdown4j plugins from application configuration.
	 * @param application Instance of Play application
	 * @param delegate Instance of delegate pages service
	 */
	public MarkdownProcessor(Application application, Service<R, S, Page> delegate) {
		
        this.application = application;
		this.delegate = delegate;
        this.processor = new Markdown4jProcessor();
        
        Configuration pluginsConf =
                application.configuration().getConfig(MARKDOWN4J_PLUGINS_CONFIGURATION_KEY);
        
        if (null != pluginsConf) {
            Map<String, Object> plugins = pluginsConf.asMap();
            for (String key : plugins.keySet()) {
                String pluginClass = (String) plugins.get(key);
                try {
                    Class<? extends Plugin> plugin = application.classloader().loadClass(pluginClass).asSubclass(Plugin.class);
                    Constructor<? extends Plugin> constructor = plugin.getConstructor(String.class);
                    this.processor = this.processor.registerPlugins(constructor.newInstance(key));
                    Logger.info("Picked markdown4j plugin: {} - {}", key, pluginClass);
                } catch (Exception ex) {
                    Logger.error("Unable to load markdown4j plugin {} - {}", pluginClass, ex.getMessage());
                }
            }
        }
	}

	/** 
     * {@inheritDoc}
     */
	@Override
	public R get(S selector) {
		return this.delegate.get(selector);
	}

	/**
	 * {@inheritDoc}
     * Converts markdown to HTML before persisting.
	 */
	@Override
	public R put(S selector, Page page) {
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
