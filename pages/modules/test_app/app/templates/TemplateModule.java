package templates;

import com.stys.platform.pages.api.Template;
import com.stys.platform.pages.api.TemplateKeys;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

public class TemplateModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq(
            bind(Template.class).qualifiedWith("blog").to(BlogTemplate.class),
            bind(Template.class).qualifiedWith("news").to(NewsTemplate.class),
            bind(Template.class).qualifiedWith(TemplateKeys.DEFAULT_TEMPLATE_KEY).to(BlogTemplate.class),
            bind(Template.class).qualifiedWith(TemplateKeys.EDITOR_TEMPLATE_KEY).to(EditorTemplate.class),
            bind(Template.class).qualifiedWith(TemplateKeys.ERROR_TEMPLATE_KEY).to(ErrorTemplate.class)
        );
    }

}
