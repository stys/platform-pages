package com.stys.platform.pages.modules;

import com.stys.platform.pages.api.TemplateProvider;
import com.stys.platform.pages.templates.EditTemplateProvider;
import com.stys.platform.pages.templates.ViewTemplateProvider;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

public class DefaultTemplatesModule extends Module {
    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq(
            bind(TemplateProvider.class).qualifiedWith("edit:template-provider").to(EditTemplateProvider.class),
            bind(TemplateProvider.class).qualifiedWith("view:template-provider").to(ViewTemplateProvider.class)
        );
    }
}
