package com.stys.platform.pages.controllers;

import com.stys.platform.pages.api.Page;
import com.stys.platform.pages.api.Selector;
import com.stys.platform.pages.api.Service;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;

import static com.stys.platform.pages.api.Result.Status.Ok;

public class Actions extends play.mvc.Controller {

    public interface ViewService extends Service<com.stys.platform.pages.api.Result<Content>, Selector, Page> {}

    private Service<com.stys.platform.pages.api.Result<Content>, Selector, Page> viewService;

    public interface EditService extends Service<com.stys.platform.pages.api.Result<Content>, Selector, Page> {}

    private Service<com.stys.platform.pages.api.Result<Content>, Selector, Page> editService;

    public interface PreviewService extends Service<com.stys.platform.pages.api.Result<Content>, Selector, Page> {}

    private Service<com.stys.platform.pages.api.Result<Content>, Selector, Page> previewService;

    @Inject
    public Actions(ViewService viewService, EditService editService, PreviewService previewService) {
        this.viewService = viewService;
        this.editService = editService;
        this.previewService = previewService;
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
    	if (result.getStatus().equals(Ok)) {
    		final String namespace = page.namespace;
            final String key = page.key;
            return redirect(routes.Actions.view(namespace, key));
        // Else -> display returned content: probably containing some description of error
    	} else {
    		return toPlayMvcResult(result);
    	}
    }

    public Result preview() {

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
        com.stys.platform.pages.api.Result<Content> result = previewService.put(new Selector(page.namespace, page.key), page);

        // Convert to MVC result
        return toPlayMvcResult(result);
    }

    private static Result toPlayMvcResult(com.stys.platform.pages.api.Result<Content> result) {
        switch (result.getStatus()) {
            case Ok:
                return Results.ok(result.getPayload());
            case BadRequest:
                return Results.badRequest(result.getPayload());
            case NotFound:
                return Results.notFound(result.getPayload());
            case Unauthorized:
                return Results.unauthorized(result.getPayload());
            case Forbidden:
                return Results.forbidden(result.getPayload());
            default:
                throw new IllegalStateException(String.format("Unexpected status: %s", result.getStatus()));
        }
    }

}
