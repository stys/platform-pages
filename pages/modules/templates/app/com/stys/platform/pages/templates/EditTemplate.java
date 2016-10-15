package com.stys.platform.pages.templates;

import com.stys.platform.pages.api.*;
import play.Configuration;
import play.twirl.api.Content;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditTemplate implements Template {

    private static final String TEMPLATES_CONFIGURATION_KEY = "pages.templates";
    private static final String CATEGORIES_CONFIGURATION_KEY = "pages.categories";

    private List<String> templates;
    private List<String> categories;

    @Inject
    public EditTemplate(Configuration configuration) {
        templates = new ArrayList<>();
        templates.addAll(configuration.getConfig(TEMPLATES_CONFIGURATION_KEY).asMap().keySet());

        categories = configuration.getStringList(CATEGORIES_CONFIGURATION_KEY);
    }

    @Override
    public Content render(Page page) {

        // State options
        List<State> states = new ArrayList<>();
        Collections.addAll(states, State.values());

        // Access options
        List<Access> access = new ArrayList<>();
        Collections.addAll(access, Access.values());

        // render editor with page content
        return com.stys.platform.pages.templates.views.html.editor.render(
            page, templates, categories, states, access);

    }

}
