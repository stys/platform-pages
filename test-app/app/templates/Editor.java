package templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import play.mvc.Content;

import com.stys.platform.pages.impl.domain.Access;
import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.State;
import com.stys.platform.pages.impl.domain.Template;

public class Editor implements Template {

	@Override
	public Content render(Page page) {
		
		// Template options
		List<String> templates = Arrays.asList("a", "b");
		
		// State options
		List<String> states = new ArrayList<>();
		for ( State s : State.values() ) {
			states.add(s.name());
		}
		
		// Access options
		List<String> access = new ArrayList<>();
		for ( Access a : Access.values() ) {
			access.add(a.name());
		}		
		
		// render editor with page content
		return views.html.editor.render(page, templates, states, access);
	}
	
}
