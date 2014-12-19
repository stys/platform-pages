package pages;

import com.stys.platform.pages.Pages;
import com.stys.platform.pages.Plugin;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Template;
import play.Application;
import play.mvc.Content;

import play.Logger;

/**
 *
 */
public class SimplePagesPlugin extends Plugin {

    private Application application;
    private Pages pages;

    public SimplePagesPlugin(Application application) {
        this.application = application;
    }

    public void onStart() {

        Logger.debug("Using SimplePagesPlugin");

        /**
         * Template service
         */
        Template<String> template = new Template<String>() {
            @Override
            public Content render(String content) {
                return views.html.template.render(content);
            }
        };

        /**
         * Repository service
         */
        Repository<String> repository = new SimpleRepository();

        this.pages = new SimplePages(template, repository);

    }

    public Pages getPagesService() {
        return this.pages;
    }

}
