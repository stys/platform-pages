package com.stys.platform.pages.impl;

import play.libs.F;

import com.stys.platform.pages.Repository;

/**
 * Implementation of pages repository over {@link PageEntity}.
 */
public class PagesRepository implements Repository<Page> {

	/**
	 * Creates a new revision, when called without a specific revision. Ohterwise
	 * overwrites the contents of the specified revision. 
	 */
	@Override
    public void put(Page page_, String namespace, String key, F.Option<Long> revision) {
		
		// Inserting new version of the page
		if (revision.isEmpty()) {
			
			PageEntity page = new PageEntity();
			page.namespace = namespace;
			page.key = key;
			page.title = page_.title;
			page.content = page_.content;
			page.template = page_.template;
			page.save();
			
		// Update existing revision (bad...)
		}  else {
			
			// Retrieve existing revision
			PageEntity page = PageEntity.find.byId(revision.get());
			
			// If revision doesn't exits - exit
			if (null == page) return;
			
			// Check that namespace and key are correct
			if (! namespace.equals(page.namespace) ) return;
			if (! key.equals(page.key) ) return;
			
			// If everything ok - update
			page.title = page_.title;
			page.template = page_.template;
			page.content = page_.content;
			page.save();
			
		}				
    }

	/**
	 * Returns a contents of the revision 
	 */
    @Override
    public F.Option<Page> get(String namespace, String key, F.Option<Long> revision) {

    	// If version is specified
    	if (revision.isDefined()) {
    		   		
    		// Find a page 
    		PageEntity page = PageEntity.find.byId(revision.get());
    		
    		// If not found
    		if (null == page) {
    			
    			return F.Option.None();
    		
    		} else {
    			
    			// Verify namespace and key
    			if (! namespace.equals(page.namespace) ) return F.Option.None();
    			if (! key.equals(page.namespace) ) return F.Option.None();
    			
    			// Return as Page
    			Page page_ = new Page();
    			page_.namespace = page.namespace;
    			page_.key = page.key;
    			page_.title = page.title;
    			page_.content = page.content;
    			page_.template = page.template;
    			return F.Option.Some(page_);
    		}
    		
    	} else { // Find latest
    	
	        // Get latest revision
	        PageEntity page = PageEntity.find.where()
	                .eq("namespace", namespace)
	                .eq("key", key)
	                .orderBy("revision desc")
	                .setMaxRows(1).findUnique();
	
	        // If not found
	        if ( null == page ) {
	        	return F.Option.None();
	        }
		                
	        // As simple page
	        Page page_ = new Page();
			page_.namespace = page.namespace;
			page_.key = page.key;
			page_.title = page.title;
			page_.content = page.content;
			page_.template = page.template;
	        return F.Option.Some(page_);
    	
    	}
    }

}
