package com.stys.platform.pages.impl.controllers;

import java.util.List;
import java.util.Map;

import com.stys.platform.pages.impl.domain.Selector;
import play.Logger;
import play.Play;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Call;
import play.mvc.Result;

import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.ContentResult;
import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.edit.EditPlugin;
import com.stys.platform.pages.impl.view.ViewPlugin;

public class Actions extends play.mvc.Controller {

    /**
     * Display a page by namespace and key
     * @param namespace
     * @param key
     * @return
     */
    public static Result view(String namespace, String key) {

    	// Get show service from plugin
        Service<ContentResult, Selector, Page> pages = Play.application().plugin(ViewPlugin.class).getPageService();
        
        // Retrieve a page
        ContentResult result = pages.get(new Selector(namespace, key));

        // Convert to play.mvc.Result
        return result.toPlayMvcResult();
    }
    
    /**
     * Page edit form
     */
    private static final Form<Page> form = Form.form(Page.class);

    /**
     * Show edit form for a page
     * @param namespace
     * @param key
     * @return
     */
    public static Result edit(String namespace, String key) {

        // Get show service
    	Service<ContentResult, Selector, Page> pages = Play.application().plugin(EditPlugin.class).getPageService();

        // Get a requested page
        ContentResult result = pages.get(new Selector(namespace, key));

        // Convert to play.mvc.Result
        return result.toPlayMvcResult();
    	
    }

    /**
     * Save form data
     * @return
     */
    public static Result update() {

        // Bind form data from request
    	Form<Page> filled = form.bindFromRequest();
        
    	// Check errors in form
    	if( filled.hasErrors() ) {
        	for (Map.Entry<String, List<ValidationError>> e : filled.errors().entrySet()) {
        		Logger.error(e.getKey() + " " + e.getValue().get(0).message());
        	}
        }
    	
    	// Get binded page data
    	final Page page = filled.get();
            	
        // Get pages edit service
        Service<ContentResult, Selector, Page> pages = Play.application().plugin(EditPlugin.class).getPageService();

  	    // Store as new revision
    	ContentResult result = pages.put(new Selector(page.namespace, page.key), page);

        // If status Ok -> redirect to view 
    	if (result.getStatus().equals(com.stys.platform.pages.Result.Status.Ok)) {
    		final String namespace = page.namespace;
            final String key = page.key;
            return redirect(new Call() {
                @Override
                public String url() {
                    return request().uri() + String.format("/%s/%s", namespace, key);
                }
                @Override
                public String method() {
                    return "GET";
                }
            });
    	// Else -> display returned content: probably containing some description of error 
    	} else {
    		return result.toPlayMvcResult();
    	}
    }

}
