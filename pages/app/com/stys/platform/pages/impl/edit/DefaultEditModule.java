package com.stys.platform.pages.impl.edit;

import com.stys.platform.pages.impl.domain.ContentService;
import com.stys.platform.pages.impl.domain.PageService;
import com.stys.platform.pages.impl.markdown4j.MarkdownProcessor;
import com.stys.platform.pages.impl.repositories.DefaultPagesRepository;
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
            bind(ContentService.class).qualifiedWith("edit:delegate").to(EditTemplateSwitcher.class),
            bind(PageService.class).qualifiedWith("edit:switcher:delegate").to(EditAccessManager.class),
            bind(PageService.class).qualifiedWith("edit:access:delegate").to(MarkdownProcessor.class),
            bind(PageService.class).qualifiedWith("processor:delegate").to(DefaultPagesRepository.class)
        );
    }
}
