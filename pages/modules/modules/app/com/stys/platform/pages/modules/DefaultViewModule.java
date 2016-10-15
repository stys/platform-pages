package com.stys.platform.pages.modules;

import com.stys.platform.pages.api.ContentService;
import com.stys.platform.pages.api.PageService;
import com.stys.platform.pages.impl.view.ViewAccessManager;
import com.stys.platform.pages.impl.view.ViewService;
import com.stys.platform.pages.impl.view.ViewTemplateService;
import com.stys.platform.pages.markdown.DefaultMarkdownPluginsProvider;
import com.stys.platform.pages.markdown.MarkdownPluginsProvider;
import com.stys.platform.pages.markdown.MarkdownProcessor;
import com.stys.platform.pages.repository.DefaultPagesRepository;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

/** */
public class DefaultViewModule extends Module {

    @Override
	public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
		return seq(
            bind(ContentService.class).qualifiedWith("view").to(ViewService.class),
            bind(ContentService.class).qualifiedWith("view:delegate").to(ViewTemplateService.class),
            bind(PageService.class).qualifiedWith("view:template:delegate").to(ViewAccessManager.class),
            bind(PageService.class).qualifiedWith("view:access:delegate").to(MarkdownProcessor.class),
            bind(PageService.class).qualifiedWith("processor:delegate").to(DefaultPagesRepository.class),
            bind(MarkdownPluginsProvider.class).to(DefaultMarkdownPluginsProvider.class)
		);
	}

}
