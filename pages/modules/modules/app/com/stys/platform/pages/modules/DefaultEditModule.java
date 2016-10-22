package com.stys.platform.pages.modules;

import com.stys.platform.pages.api.*;
import com.stys.platform.pages.impl.DefaultTemplateProvider;
import com.stys.platform.pages.impl.EditAccessManager;
import com.stys.platform.pages.impl.EditService;
import com.stys.platform.pages.impl.EditTemplateService;
import com.stys.platform.pages.markdown.DefaultMarkdownPluginsProvider;
import com.stys.platform.pages.markdown.MarkdownPluginsProvider;
import com.stys.platform.pages.markdown.MarkdownProcessor;
import com.stys.platform.pages.repository.DefaultPagesRepository;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

public class DefaultEditModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq(
            bind(ContentService.class).qualifiedWith("edit").to(EditService.class),
            bind(ContentService.class).qualifiedWith("edit:delegate").to(EditTemplateService.class),
            bind(TemplateProvider.class).qualifiedWith("edit:template-provider").to(DefaultTemplateProvider.class),
            bind(PageService.class).qualifiedWith("edit:template:delegate").to(EditAccessManager.class),
            bind(PageService.class).qualifiedWith("edit:access:delegate").to(MarkdownProcessor.class),
            bind(PageService.class).qualifiedWith("processor:delegate").to(DefaultPagesRepository.class),
            bind(MarkdownPluginsProvider.class).to(DefaultMarkdownPluginsProvider.class)
        );

    }
}
