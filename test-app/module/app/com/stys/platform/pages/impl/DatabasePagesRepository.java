package com.stys.platform.pages.impl;

import play.Application;
import play.libs.F;

import com.stys.platform.pages.Result;
import com.stys.platform.pages.Results;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.Access;
import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.State;

/**
 * Implementation of repository
 */
public class DatabasePagesRepository extends Results implements Service<Result<Page>, Page> {

	/**
	 * Conventional injecting constructor
	 */
	public DatabasePagesRepository(Application application, Service<Result<Page>, Page> delegate) {
		/* Empty */
	}
	
	/**
	 * Creates a new revision, when called without a specific revision. Otherwise
	 * overwrites the contents of the specified revision. 
	 */
	@Override
    public Result<Page> put(Page page, String namespace, String key, F.Option<Long> revision) {
						
		if (revision.isEmpty()) {

			// Try to find page entity
			com.stys.platform.pages.impl.models.Page entity = com.stys.platform.pages.impl.models.Page.find.where()
					.eq("namespace", namespace)
					.eq("key", key)
					.findUnique();

			if (null == entity) {
				// new page
				com.stys.platform.pages.impl.models.Page.create(namespace, key, page);

			} else {
				
				// update metadata
				entity.access = page.access.name();
				entity.state = page.state.name();
				entity.template = page.template;
				entity.save();
				
				// check that content changed
				// FIXME: should not create new revision if content did not change 
				
				// new revision of existing page
				com.stys.platform.pages.impl.models.Revision.create(entity, page.title, page.source, page.content);
			}
			
			return get(namespace, key, revision);	

		}  else {
			// Updating existing revision is a very bad idea
			throw new UnsupportedOperationException("Cannot update existing revision");
		}				

	}

	/**
	 * Returns a contents of the page
	 */
    @Override
    public Result<Page> get(String namespace, String key, F.Option<Long> revision) {

		// Lookup the page
		com.stys.platform.pages.impl.models.Page entity = 
			com.stys.platform.pages.impl.models.Page.find.where()
				.eq("namespace", namespace)
				.eq("key", key)
				.findUnique();

		// Check page is found
		if (null == entity) {
			
			// Stub return page
			Page stub = new Page();
			stub.namespace = namespace;
			stub.key = key;
								
			return NotFound(stub);
		}

		// Find revision
		com.stys.platform.pages.impl.models.Revision rev = null;

		// If specific revision requested
		if (revision.isDefined()) {

			// Find by revision id
			rev = com.stys.platform.pages.impl.models.Revision.find.byId(revision.get());

		// Otherwise fetch latest revision
		} else {

			rev = com.stys.platform.pages.impl.models.Revision.find.where()
					.eq("page", entity)
					.orderBy("revision desc")
					.setMaxRows(1)
					.findUnique();
			
		}

		if (null == rev) {
			return NotFound(null);
		}

		// Convert to Page
		return Ok(fromEntity(entity, rev));	
    }
    
    private static Page fromEntity(
    		com.stys.platform.pages.impl.models.Page entity, 
    		com.stys.platform.pages.impl.models.Revision revision) {
    	
    	Page page = new Page();    	
    	
    	page.namespace = entity.namespace;
		page.key = entity.key;
		page.access = Access.valueOf(entity.access);
		page.state = State.valueOf(entity.state);
		page.template = entity.template;

		page.revision = revision.revision;
		page.title = revision.title;
		page.source = revision.source;
		page.content = revision.content;
		
		return page;		
    }
        
}
