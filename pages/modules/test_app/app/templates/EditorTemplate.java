package templates;

import com.stys.platform.pages.api.*;
import play.Configuration;
import play.twirl.api.Content;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class EditorTemplate implements Template {

    private static final String CATEGORIES_CONFIGURATION_KEY = "pages.categories";

    private static final String TEMPLATES_CONFIGURATION_KEY = "pages.templates";

    private List<String> templates;

    private List<String> categories;

    @Inject
    public EditorTemplate(Configuration configuration) {
        templates = configuration.getStringList(TEMPLATES_CONFIGURATION_KEY);
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
        return views.html.editor.render(page, templates, categories, states, access);

    }

}
