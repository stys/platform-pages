package templates;

import play.mvc.Content;

import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.Template;

public class TemplateA implements Template {

    @Override
    public Content render(Page page) {
        return views.html.a.render(page);
    }

}
