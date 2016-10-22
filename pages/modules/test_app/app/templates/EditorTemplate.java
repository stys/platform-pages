package templates;

import com.stys.platform.pages.api.*;
import play.Configuration;
import play.twirl.api.Content;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.stys.platform.pages.api.TemplateKeys.ENABLED_TEMPLATES_KEY;

public class EditorTemplate implements Template {

    private static final String CATEGORIES_CONFIGURATION_KEY = "pages.categories";

    private List<String> templates;
    private List<String> categories;

    public EditorTemplate(Configuration configuration) {
        templates = configuration.getConfig(ENABLED_TEMPLATES_KEY).asMap().keySet().stream()
                .map(t -> ENABLED_TEMPLATES_KEY + '.' + t).collect(Collectors.toList());
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
