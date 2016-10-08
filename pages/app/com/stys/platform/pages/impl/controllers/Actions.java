package com.stys.platform.pages.impl.controllers;

import com.stys.platform.pages.impl.domain.ContentService;
import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.Selector;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Call;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Actions extends play.mvc.Controller {

    private ContentService viewService;

    private ContentService editService;

    @Inject
    public Actions(
        @Named("view") ContentService viewService,
        @Named("edit") ContentService editService
    ) {
        this.viewService = viewService;
        this.editService = editService;
    }

    /** Display a page by namespace and key */
    public Result view(String namespace, String key) {
        
        // Retrieve a page
        com.stys.platform.pages.api.Result<Content> result = viewService.get(new Selector(namespace, key));

        // Convert to play.mvc.Result
        return toPlayMvcResult(result);
    }
    
    /** Page edit form */
    private static final Form<Page> form = Form.form(Page.class);

    /** Display edit form for a page */
    public Result edit(String namespace, String key) {

        // Get a requested page
        com.stys.platform.pages.api.Result<Content> result = editService.get(new Selector(namespace, key));

        // Convert to play.mvc.Result
        return toPlayMvcResult(result);
    }

    /** Handle page edit form data */
    public Result update() {

        // Bind form data from request
    	Form<Page> filled = form.bindFromRequest();
        
    	// Check errors in form
    	if( filled.hasErrors() ) {
        	for (Map.Entry<String, List<ValidationError>> e : filled.errors().entrySet()) {
        		Logger.error(e.getKey() + " " + e.getValue().get(0).message());
        	}
        }
    	
    	// Get page data
    	final Page page = filled.get();

  	    // Store as new revision
        com.stys.platform.pages.api.Result<Content> result = editService.put(new Selector(page.namespace, page.key), page);

        // If status Ok -> redirect to view 
    	if (result.getStatus().equals(com.stys.platform.pages.api.Result.Status.Ok)) {
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

                @Override
                public String fragment() {
                    return "";
                }
            });

        // Else -> display returned content: probably containing some description of error
    	} else {
    		return toPlayMvcResult(result);
    	}
    }

    private static Result toPlayMvcResult(com.stys.platform.pages.api.Result<Content> result) {
        switch (result.getStatus()) {
            case Ok:
                return Results.ok(result.getContent());
            case BadRequest:
                return Results.badRequest(result.getContent());
            case NotFound:
                return Results.notFound(result.getContent());
            case Unauthorized:
                return Results.unauthorized(result.getContent());
            case Forbidden:
                return Results.forbidden(result.getContent());
            default:
                throw new IllegalStateException(String.format("Unexpected status: %s", result.getStatus()));
        }
    }

}
