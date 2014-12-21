package templates;

import play.mvc.Content;

import com.stys.platform.pages.Template;
import com.stys.platform.pages.impl.PageEdit;

public class EditorTemplate implements Template<PageEdit> {

	@Override
	public Content render(PageEdit edit) {
		return views.html.editor.render(edit);
	}
	
}
