package templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import play.twirl.api.Content;

import com.stys.platform.pages.impl.domain.Access;
import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.State;
import com.stys.platform.pages.impl.domain.Template;

public class Editor implements Template {

	@Override
	public Content render(Page page) {
		
		// Template options
		List<String> templates = Arrays.asList("a", "b");
		
		// Category options
		List<String> category = Arrays.asList("Новости CubicPrints", 
											  "Интервью CubicPrints",
											  "События CubicPrints",
											  "Конкурсы CubicPrints",
											  "3D-печать в мире");
		
		// State options
		List<State> states = new ArrayList<>();
		for ( State s : State.values() ) {
			states.add(s);
		}
		
		// Access options
		List<Access> access = new ArrayList<>();
		for ( Access a : Access.values() ) {
			access.add(a);
		}		
		
		// render editor with page content
		return views.html.editor.render(page, templates, category, states, access);
	}
	
}
