package templates;

import play.mvc.Content;

import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.Template;

public class NotFound implements Template {

	@Override
	public Content render(Page page) {
		return views.html.notfound.render(page.namespace, page.key);
	}

	
}
