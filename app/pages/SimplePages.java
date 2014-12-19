package pages;

import com.stys.platform.pages.Pages;
import com.stys.platform.pages.Repository;
import com.stys.platform.pages.Template;
import play.libs.F;
import play.mvc.Content;

/**
 *
 */
public class SimplePages extends Pages {
    public SimplePages(Template<String> template, Repository<String> repository) {
        super(template, repository);
    }
}
