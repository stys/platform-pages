package com.stys.platform.pages.modules;

import com.stys.platform.pages.api.*;
import com.stys.platform.pages.controllers.Actions;
import com.stys.platform.pages.impl.*;
import com.stys.platform.pages.markdown.DefaultMarkdownPluginsProvider;
import com.stys.platform.pages.markdown.MarkdownPluginsProvider;
import com.stys.platform.pages.markdown.DefaultMarkdownProcessor;
import com.stys.platform.pages.markdown.MarkdownProcessor;
import com.stys.platform.pages.repository.DefaultPagesRepository;
import com.stys.platform.pages.utils.InjectServiceAdapter;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import play.twirl.api.Content;
import scala.collection.Seq;

import javax.inject.Inject;
import javax.inject.Singleton;

public class DefaultEditModule extends Module {

    @Singleton
    private static class _DefaultEditService
            extends InjectServiceAdapter<Result<Content>, Selector, Page, DefaultEditService>
            implements Actions.EditService {}

    @Singleton
    private static class _DefaultEditAccessManager
            extends InjectServiceAdapter<Result<Page>, Selector, Page, DefaultEditAccessManager>
            implements DefaultEditService.Delegate {}

    @Singleton
    private static class _MarkdownProcessor implements DefaultEditAccessManager.Delegate {
        @Inject
        private MarkdownProcessor processor;

        @Inject
        private DefaultPagesRepository repository;

        @Override
        public Result<Page> get(Selector selector) {
            return repository.get(selector);
        }

        @Override
        public Result<Page> put(Selector selector, Page page) {
            page.content = processor.process(page.source);
            return repository.put(selector, page);
        }
    }

    @Singleton
    private static class _DefaultTemplateProvider implements DefaultEditService.EditTemplateProvider {
        @Inject
        private DefaultTemplateProvider instance;

        @Override
        public Template get(String name) {
            return instance.get(name);
        }
    }

    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq(
            bind(Actions.EditService.class).to(_DefaultEditService.class),
            bind(DefaultEditService.EditTemplateProvider.class).to(_DefaultTemplateProvider.class),
            bind(DefaultEditService.Delegate.class).to(_DefaultEditAccessManager.class),
            bind(DefaultEditAccessManager.Delegate.class).to(_MarkdownProcessor.class),
            bind(MarkdownProcessor.class).to(DefaultMarkdownProcessor.class),
            bind(MarkdownPluginsProvider.class).to(DefaultMarkdownPluginsProvider.class)
        );

    }
}
