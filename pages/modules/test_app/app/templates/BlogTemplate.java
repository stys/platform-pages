package templates;


import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Template;
import play.Configuration;
import play.twirl.api.Content;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class BlogTemplate implements Template {

    @Override
    public Content render(Page page) {
        return views.html.blog.render(page);
    }

}
