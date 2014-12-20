package templates;

import play.mvc.Content;

import com.stys.platform.pages.Template;

public class OldTemplate implements Template<String> {

    @Override
    public Content render(String content) {
        return views.html.old.render(content);
    }

}
