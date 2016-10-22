package templates;


import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Template;
import play.Configuration;
import play.twirl.api.Content;

public class ErrorTemplate implements Template {

    private Configuration configuration;

    public ErrorTemplate(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Content render(Page page) {
        return null;
    }
}
