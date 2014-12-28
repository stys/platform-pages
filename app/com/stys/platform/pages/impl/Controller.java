package com.stys.platform.pages.impl;

import play.Logger;
import play.Play;
import play.data.Form;
import play.libs.F;
import play.mvc.Content;
import play.mvc.Result;

import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.edit.EditPlugin;
import com.stys.platform.pages.impl.view.ViewPlugin;

import controllers.routes;

public class Controller extends play.mvc.Controller {

	public static F.None<Long> None = new F.None<>();
	
    public static Result index() {
        return ok("Welcome to Platform Pages");
    }

    /**
     * Display a page by namespace and key
     * @param namespace
     * @param key
     * @return
     */
    public static Result pages(String namespace, String key) {

    	// Get pages service from plugin
        Service<Page> pages = Play.application().plugin(ViewPlugin.class).getPagesService();
        
        // Retrieve a page
        F.Option<Content> page = pages.get(namespace, key, None);

        // Check that page exists -> not found
        if (page.isEmpty()) return notFound("Not found");
        
        // Ok
        else return ok(page.get());
    }
    
    /**
     * Page edit form
     */
    private static final Form<Page> form = Form.form(Page.class);
     
    /**
     * Create new page at a given workspace and key
     * @param namespace
     * @param key
     * @return
     */
    public static Result create(String namespace, String key) {
           	
    	// Creating new pages is handled transparently by the service
    	return redirect(com.stys.platform.pages.impl.routes.Controller.edit(namespace, key));
    	
    }

    /**
     * Show edit form for a page
     * @param namespace
     * @param key
     * @return
     */
    public static Result edit(String namespace, String key) {

        // Get pages service
    	Service<Page> pages = Play.application().plugin(EditPlugin.class).getPagesService();

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
    public static Result save() {

        // Bind form data from request
    	Form<Page> filled = form.bindFromRequest();
        Page page = filled.get();
        
        Logger.info(page.toString());
        
        // Retrieve pages service
        Service<Page> pages = Play.application().plugin(EditPlugin.class).getPagesService();

  	    // Store as new revision
    	pages.put(page, page.namespace, page.key, None);

        // Redirect to public
    	return redirect(com.stys.platform.pages.impl.routes.Controller.pages(page.namespace, page.key));

    }

}
