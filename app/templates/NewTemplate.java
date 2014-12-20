package templates;

import play.mvc.Content;

import com.stys.platform.pages.Template;

public class NewTemplate implements Template<String> {

    @Override
    public Content render(String content) {
        return views.html.new_.render(content);
    }

}
