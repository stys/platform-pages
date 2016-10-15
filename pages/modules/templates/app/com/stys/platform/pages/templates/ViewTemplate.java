package com.stys.platform.pages.templates;


import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Template;
import play.twirl.api.Content;

public class ViewTemplate implements Template {

    @Override
    public Content render(Page page) {
        return com.stys.platform.pages.templates.views.html.basic.render(page);
    }
}
