package com.stys.platform.pages.modules;

import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Result;
import com.stys.platform.pages.api.Selector;
import com.stys.platform.pages.api.Template;
import com.stys.platform.pages.controllers.Actions;
import com.stys.platform.pages.impl.DefaultTemplateProvider;
import com.stys.platform.pages.impl.DefaultViewAccessManager;
import com.stys.platform.pages.impl.DefaultViewService;
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

public class DefaultViewModule extends Module {

    @Singleton
    private static class _DefaultViewService
            extends InjectServiceAdapter<Result<Content>, Selector, Page, DefaultViewService>
            implements Actions.ViewService {}

    @Singleton
    private static class _DefaultViewAccessManager
            extends InjectServiceAdapter<Result<Page>, Selector, Page, DefaultViewAccessManager>
            implements DefaultViewService.Delegate {}

    @Singleton
    private static class _DefaultPagesRepository
            extends InjectServiceAdapter<Result<Page>, Selector, Page, DefaultPagesRepository>
            implements DefaultViewAccessManager.Delegate {}

    @Singleton
    private static class _DefaultTemplateProvider implements DefaultViewService.ViewTemplateProvider {
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
            bind(Actions.ViewService.class).to(_DefaultViewService.class),
            bind(DefaultViewService.Delegate.class).to(_DefaultViewAccessManager.class),
            bind(DefaultViewService.ViewTemplateProvider.class).to(_DefaultTemplateProvider.class),
            bind(DefaultViewAccessManager.Delegate.class).to(_DefaultPagesRepository.class)
		);
	}

}
