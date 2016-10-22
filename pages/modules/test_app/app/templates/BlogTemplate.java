package templates;


import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Template;
import play.Configuration;
import play.twirl.api.Content;

public class BlogTemplate implements Template {

    private Configuration configuration;

    public BlogTemplate(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Content render(Page page) {
        return views.html.blog.render(page);
    }
}
