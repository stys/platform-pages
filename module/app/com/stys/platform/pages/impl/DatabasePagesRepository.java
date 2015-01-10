package com.stys.platform.pages.impl;

import play.Application;
import play.libs.F;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxRunnable;
import com.stys.platform.pages.Result;
import com.stys.platform.pages.Results;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.Access;
import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.State;
import com.stys.platform.pages.impl.models.Revision;

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
    public Result<Page> put(final Page page, String namespace, String key, F.Option<Long> revision) {
						
		if (revision.isEmpty()) {

			// Try to find page entity
			final com.stys.platform.pages.impl.models.Page entity = com.stys.platform.pages.impl.models.Page.find.where()
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
								
				// check that content changed
				// FIXME: should not create new revision if content did not change 
				
				// Transactional save 
		        Ebean.execute(new TxRunnable() {
		            @Override
		            public void run() {
		            	// new revision of existing page
						Revision rev = com.stys.platform.pages.impl.models.Revision.create(entity, page.title, page.source, page.content);
						// set active revision
						entity.revision = rev;
						entity.save();		
		            }
		        });			
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
		com.stys.platform.pages.impl.models.Revision rev = entity.revision;

		// If specific revision requested
		if (revision.isDefined()) {

			// Find by revision id
			rev = com.stys.platform.pages.impl.models.Revision.find.byId(revision.get());

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

		page.revision = revision.id;
		page.title = revision.title;
		page.source = revision.source;
		page.content = revision.content;
		
		return page;		
    }
        
}
