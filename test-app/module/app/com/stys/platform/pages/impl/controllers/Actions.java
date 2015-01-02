package com.stys.platform.pages.impl.controllers;

import play.Play;
import play.data.Form;
import play.libs.F;
import play.mvc.Result;

import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.Page;
import com.stys.platform.pages.impl.edit.EditPlugin;
import com.stys.platform.pages.impl.utils.ContentResult;
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
    public static Result view(String namespace, String key) {

    	// Get show service from plugin
        Service<ContentResult, Page> pages = Play.application().plugin(ViewPlugin.class).getPagesService();
        
        // Retrieve a page
        ContentResult result = pages.get(namespace, key, None);

        // Convert to play.mvc.Result
        return result.toPlayMvcResult();
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
    public static Result edit(String namespace, String key) {

        // Get show service
    	Service<ContentResult, Page> pages = Play.application().plugin(EditPlugin.class).getPagesService();

        // Get a requested page
        ContentResult result = pages.get(namespace, key, None);

        // Convert to play.mvc.Result
        return result.toPlayMvcResult();
    	
    }

    /**
     * Save form data
     * @return
     */
    public static Result save() {

        // Bind form data from request
    	Form<Page> filled = form.bindFromRequest();
        Page page = filled.get();
        
        // Retrieve show service
        Service<ContentResult, Page> pages = Play.application().plugin(EditPlugin.class).getPagesService();

  	    // Store as new revision
    	ContentResult result = pages.put(page, page.namespace, page.key, None);

        // If status Ok -> redirect to view 
    	if (result.getStatus().equals(com.stys.platform.pages.Result.Status.Ok)) {
    		return view(page.namespace, page.key);
    	// Else -> display returned content: probably containing some description of error 
    	} else {
    		return result.toPlayMvcResult();
    	}
    }

}
