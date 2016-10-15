package com.stys.platform.pages.templates;

import com.stys.platform.pages.api.Template;
import com.stys.platform.pages.api.TemplateProvider;
import play.Configuration;
import play.Environment;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class ViewTemplateProvider implements TemplateProvider {

    private static final String TEMPLATES_CONFIGURATION_KEY = "pages.templates";

    private Map<String, Template> templates = new HashMap<>();

    @Inject
    public ViewTemplateProvider(Configuration configuration, Environment environment) {

        Map<String, Object> templatesConfiguration =
                configuration.getConfig(TEMPLATES_CONFIGURATION_KEY).asMap();

        for (Map.Entry<String, Object> t: templatesConfiguration.entrySet()) {
            try {
                // Load template class
                Template template =
                    (Template) environment.classLoader().loadClass((String) t.getValue()).newInstance();

                // Add to collection of templates
                templates.put(t.getKey(), template);

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    @Override
    public Template get(String name) {
        return this.templates.get(name);
    }
}
