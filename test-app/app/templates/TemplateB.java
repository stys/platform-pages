package templates;

import play.twirl.api.Content;

import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.Template;

public class TemplateB implements Template {

    @Override
    public Content render(Page page) {
        return views.html.b.render(page);
    }

}
