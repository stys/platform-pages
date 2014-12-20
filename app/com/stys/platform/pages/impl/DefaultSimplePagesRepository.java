package com.stys.platform.pages.impl;

import play.libs.F;

import com.stys.platform.pages.Repository;

/**
 * Implementation of pages repository over {@link SimplePageEntity}.
 */
public class DefaultSimplePagesRepository implements Repository<SimplePage> {

	/**
	 * Creates a new revision, when called without a specific revision. Ohterwise
	 * overwrites the contents of the specified revision. 
	 */
	@Override
    public void put(String namespace, String key, F.Option<Long> revision, SimplePage page_) {
		
		// Inserting new version of the page
		if (revision.isEmpty()) {
			
			SimplePageEntity page = new SimplePageEntity();
			page.namespace = namespace;
			page.key = key;
			page.template = page_.template;
			page.content = page_.content;
			page.save();
			
		// Update existing revision (bad...)
		}  else {
			
			// Retrieve existing revision
			SimplePageEntity page = SimplePageEntity.find.byId(revision.get());
			
			// If revision doesn't exits - exit
			if (null == page) return;
			
			// Check that namespace and key are correct
			if (! namespace.equals(page.namespace) ) return;
			if (! key.equals(page.key) ) return;
			
			// If everything ok - update
			page.template = page_.template;
			page.content = page_.content;
			page.save();
			
		}				
    }

	/**
	 * Returns a contents of the revision 
	 */
    @Override
    public F.Option<SimplePage> get(String namespace, String key, F.Option<Long> revision) {

    	// If version is specified
    	if (revision.isDefined()) {
    		   		
    		// Find a page 
    		SimplePageEntity page = SimplePageEntity.find.byId(revision.get());
    		
    		// If not found
    		if (null == page) {
    			
    			return F.Option.None();
    		
    		} else {
    			
    			// Verify namespace and key
    			if (! namespace.equals(page.namespace) ) return F.Option.None();
    			if (! key.equals(page.namespace) ) return F.Option.None();
    			
    			// Return as SimplePage
    			SimplePage page_ = new SimplePage();
    			page_.template = page.template;
    			page_.content = page.content;
    			return F.Option.Some(page_);
    		}
    		
    	} else { // Find latest
    	
	        // Get latest revision
	        SimplePageEntity page = SimplePageEntity.find.where()
	                .eq("namespace", namespace)
	                .eq("key", key)
	                .orderBy("revision desc")
	                .setMaxRows(1).findUnique();
	
	        // If not found
	        if ( null == page ) {
	        	return F.Option.None();
	        }
		                
	        // As simple page
	        SimplePage page_ = new SimplePage();
			page_.template = page.template;
			page_.content = page.content;
	        return F.Option.Some(page_);
    	
    	}
    }

}
