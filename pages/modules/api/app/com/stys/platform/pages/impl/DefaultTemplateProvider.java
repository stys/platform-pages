package com.stys.platform.pages.impl;

import com.stys.platform.pages.api.Template;
import com.stys.platform.pages.api.TemplateProvider;
import play.Configuration;
import play.Environment;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static com.stys.platform.pages.api.TemplateKeys.*;

public class DefaultTemplateProvider implements TemplateProvider {

    private Map<String, Template> enabledTemplates;
    private Template defaultTemplate;
    private Template editorTemplate;
    private Template errorTemplate;

    @Inject
    @SuppressWarnings("unchecked")
    public DefaultTemplateProvider(Configuration configuration, Environment environment) {

        Map<String, Object> templatesConfiguration =
                configuration.getConfig(ENABLED_TEMPLATES_KEY).asMap();

        try {
            ClassLoader classLoader = environment.classLoader();

            Class errorTemplateClass = classLoader.loadClass(configuration.getString(ERROR_TEMPLATE_KEY));
            errorTemplate = (Template) errorTemplateClass.getConstructor(Configuration.class).newInstance(configuration);

            Class defaultTemplateClass = classLoader.loadClass(configuration.getString(DEFAULT_TEMPLATE_KEY));
            defaultTemplate = (Template) defaultTemplateClass.getConstructor(Configuration.class).newInstance(configuration);

            Class editorTemplateClass = classLoader.loadClass(configuration.getString(EDITOR_TEMPLATE_KEY));
            editorTemplate = (Template) editorTemplateClass.getConstructor(Configuration.class).newInstance(configuration);

            enabledTemplates = new HashMap<>();
            for (Map.Entry<String, Object> t: templatesConfiguration.entrySet()) {
                Class templateClass = classLoader.loadClass((String) t.getValue());
                Template template = (Template) templateClass.getConstructor(Configuration.class).newInstance(configuration);
                enabledTemplates.put(ENABLED_TEMPLATES_KEY + '.' + t.getKey(), template);
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public Template get(String name) {
        if (null == name) return this.defaultTemplate;
        switch (name) {
            case ERROR_TEMPLATE_KEY:
                return this.errorTemplate;
            case EDITOR_TEMPLATE_KEY:
                return  this.editorTemplate;
            default:
                Template template = this.enabledTemplates.get(name);
                if (null != template) return template;
                else return this.defaultTemplate;
        }
    }
}
