package com.stys.platform.pages.impl;

import com.stys.platform.pages.api.Template;
import com.stys.platform.pages.api.TemplateKeys;
import com.stys.platform.pages.api.TemplateProvider;
import play.api.inject.BindingKey;
import play.inject.Injector;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.stys.platform.pages.api.TemplateKeys.*;

@Singleton
public class DefaultTemplateProvider implements TemplateProvider {

    @Inject
    @Named(TemplateKeys.DEFAULT_TEMPLATE_KEY)
    private Template defaultTemplate;

    @Inject
    @Named(TemplateKeys.EDITOR_TEMPLATE_KEY)
    private Template editorTemplate;

    @Inject
    @Named(TemplateKeys.ERROR_TEMPLATE_KEY)
    private Template errorTemplate;

    @Inject
    private Injector injector;

    @Override
    public Template get(String name) {
        switch (name) {
            case ERROR_TEMPLATE_KEY:
                return errorTemplate;
            case EDITOR_TEMPLATE_KEY:
                return editorTemplate;
            default:
                try {
                    return injector.instanceOf(BindingKey.apply(Template.class).qualifiedWith(name));
                } catch (Exception ex) {
                    return defaultTemplate;
                }
        }
    }
}
