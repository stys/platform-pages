package com.stys.platform.pages.impl;

import play.Logger;
import play.Play;
import play.data.Form;
import play.libs.F;
import play.mvc.Content;

import com.stys.platform.pages.Result;

import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.edit.EditPlugin;
import com.stys.platform.pages.impl.view.ViewPlugin;

public class Actions extends play.mvc.Controller {

	public static F.None<Long> None = new F.None<>();
	
    public static play.mvc.Result index() {
        return ok("Welcome to Platform Pages");
    }

    /**
     * Display a page by namespace and key
     * @param namespace
     * @param key
     * @return
     */
    public static play.mvc.Result show(String namespace, String key) {

    	// Get show service from plugin
        Service<Page> pages = Play.application().plugin(ViewPlugin.class).getPagesService();
        
        // Retrieve a page
        Result result = pages.get(namespace, key, None);

        // Check that page exists -> not found
        if (result.getStatus().equals(Result.Status.NotFound))
            return notFound(result.getContent());

        // If not authorized
        if (result.getStatus().equals(Result.Status.Forbidden))
            return forbidden(result.getContent());

        // Check ok
        if (result.getStatus().equals(Result.Status.Ok))
            return ok(result.getContent());

        // Throw
        throw new IllegalStateException("Unexpected status from pages service");
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
    public static play.mvc.Result create(String namespace, String key) {
           	
    	// Creating new show is handled transparently by the service
    	return edit(namespace, key);
    	
    }

    /**
     * Show edit form for a page
     * @param namespace
     * @param key
     * @return
     */
    public static play.mvc.Result edit(String namespace, String key) {

        // Get show service
    	Service<Page> pages = Play.application().plugin(EditPlugin.class).getPagesService();

        // Get a requested page
        Result result = pages.get(namespace, key, None);

        // Check that page exists -> not found
        if (result.getStatus().equals(Result.Status.NotFound))
            return notFound(result.getContent());

        // If not authorized
        if (result.getStatus().equals(Result.Status.Forbidden))
            return forbidden(result.getContent());

        // Check ok
        if (result.getStatus().equals(Result.Status.Ok))
            return ok(result.getContent());

        // Throw
        throw new IllegalStateException("Unexpected status code");
    	
    }

    /**
     * Save form data
     * @return
     */
    public static play.mvc.Result save() {

        // Bind form data from request
    	Form<Page> filled = form.bindFromRequest();
        Page page = filled.get();
        
        // Retrieve show service
        Service<Page> pages = Play.application().plugin(EditPlugin.class).getPagesService();

  	    // Store as new revision
    	Result result = pages.put(page, page.namespace, page.key, None);

        // Check for error
        if (result.getStatus().equals(Result.Status.Forbidden))
            return forbidden(result.getContent());

        // Redirect to public
    	if (result.getStatus().equals(Result.Status.Ok))
            return show(page.namespace, page.key);

        // Unknown status
        throw new IllegalStateException("Unexpected status code");

    }

}
