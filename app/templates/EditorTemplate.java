package templates;

import com.stys.platform.pages.impl.Page;
import play.mvc.Content;

import com.stys.platform.pages.Template;

import java.util.Set;

public class EditorTemplate implements Template<Page> {

	private Set<String> templates;

	public EditorTemplate(Set<String> templates) {
		this.templates = templates;
	}

	@Override
	public Content render(Page page) {

		return views.html.editor.render(page, templates);

	}
	
}
