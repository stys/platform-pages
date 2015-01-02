package templates;

import play.mvc.Content;

import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.Page;

public class NewTemplate implements Template<Page> {

    @Override
    public Content render(Page page) {
        return views.html.new_.render(page);
    }

}
