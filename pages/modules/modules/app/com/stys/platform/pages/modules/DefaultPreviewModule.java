package com.stys.platform.pages.modules;

import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Result;
import com.stys.platform.pages.api.Selector;
import com.stys.platform.pages.api.Template;
import com.stys.platform.pages.controllers.Actions;
import com.stys.platform.pages.impl.DefaultPreviewService;
import com.stys.platform.pages.impl.DefaultTemplateProvider;
import com.stys.platform.pages.markdown.MarkdownProcessor;
import com.stys.platform.pages.utils.InjectServiceAdapter;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import play.twirl.api.Content;
import scala.collection.Seq;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.stys.platform.pages.api.Result.Status.Ok;

public class DefaultPreviewModule extends Module {

    @Singleton
    private static class _DefaultPreviewService
            extends InjectServiceAdapter<Result<Content>, Selector, Page, DefaultPreviewService>
            implements Actions.PreviewService {}

    @Singleton
    private static class _DefaultTemplateProvider implements DefaultPreviewService.ViewTemplateProvider {
        @Inject
        private DefaultTemplateProvider instance;

        @Override
        public Template get(String name) {
            return instance.get(name);
        }
    }

    @Singleton
    private static class _MarkdownProcessor implements DefaultPreviewService.Delegate {
        @Inject
        private MarkdownProcessor processor;

        @Override
        public Result<Page> get(Selector selector) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Result<Page> put(Selector selector, Page page) {
            page.content = processor.render(page.source);
            return Result.of(Ok, page);
        }
    }

    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq(
            bind(Actions.PreviewService.class).to(_DefaultPreviewService.class),
            bind(DefaultPreviewService.Delegate.class).to(_MarkdownProcessor.class),
            bind(DefaultPreviewService.ViewTemplateProvider.class).to(_DefaultTemplateProvider.class)
        );
    }

}
