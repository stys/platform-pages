package templates;

import play.mvc.Content;

import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.Page;

public class TemplateB implements Template<Page> {

    @Override
    public Content render(Page page) {
        return views.html.b.render(page);
    }

}
