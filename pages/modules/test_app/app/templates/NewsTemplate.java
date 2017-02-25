package templates;


import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Template;
import play.twirl.api.Content;

import javax.inject.Singleton;

@Singleton
public class NewsTemplate implements Template {

    @Override
    public Content render(Page page) {
        return views.html.blog.render(page);
    }

}
