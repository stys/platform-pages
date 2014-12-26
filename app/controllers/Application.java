package controllers;

import play.Logger;
import play.Play;
import play.data.Form;
import play.libs.F;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;

import com.stys.platform.pages.impl.EditPagesPlugin;
import com.stys.platform.pages.impl.Page;
import com.stys.platform.pages.impl.PagesPlugin;
import com.stys.platform.pages.impl.PagesService;

public class Application extends Controller {

	public static F.None<Long> None = new F.None<>();
	
    public static Result index() {
        return ok("Welcome");
    }

    public static Result pages(String namespace, String key) {

        Logger.debug(namespace);
        Logger.debug(key);

        PagesService pages = Play.application().plugin(PagesPlugin.class).getPagesService();
        F.Option<Content> page = pages.get(namespace, key, None);

        if (page.isEmpty()) return notFound("Not found");
        else return ok(page.get());

    }
    
    private static final Form<Page> form = Form.form(Page.class);
     
    public static Result create(String namespace, String key) {
        	   	
    	PagesService pages = Play.application().plugin(EditPagesPlugin.class).getPagesService();
    	Page page = new Page();
    	page.namespace = namespace;
    	page.key = key;
    	
    	pages.put(page, namespace, key, None);
    	
    	return redirect(routes.Application.editor(namespace, key));
    	
    }

    /**
     * Show editor page
     * @param namespace
     * @param key
     * @return
     */
    public static Result editor(String namespace, String key) {

        // Get pages service
    	PagesService pages = Play.application().plugin(EditPagesPlugin.class).getPagesService();

        // Get a requested page
        F.Option<Content> page = pages.get(namespace, key, None);

        // Check that page exists
    	if (page.isEmpty()) return notFound("Not found");

        // Return a rendered page
        else return ok(page.get());
    }

    /**
     * Save form data
     * @return
     */
    public static Result edit() {

        // Bind form data from request
    	Form<Page> filled = form.bindFromRequest();
        Page page = filled.get();

        // Retrieve pages service
        PagesService pages = Play.application().plugin(EditPagesPlugin.class).getPagesService();

  	    // Store as new revision
    	pages.put(page, page.namespace, page.key, None);

        // Redirect to public
    	return redirect(routes.Application.pages(page.namespace, page.key));

    }

}
