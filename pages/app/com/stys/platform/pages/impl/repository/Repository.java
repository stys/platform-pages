package com.stys.platform.pages.impl.repository;

import com.stys.platform.pages.impl.models.MetaEntity;
import com.stys.platform.pages.impl.models.PageEntity;
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
import com.stys.platform.pages.impl.models.RevisionEntity;

/**
 * Implementation of repository
 */
public class Repository extends Results implements Service<Result<Page>, Page> {

	/**
	 * Constructs new instance
	 */
	public Repository(Application application, Service<Result<Page>, Page> delegate) {
		/* Empty */
	}
	
	/**
	 * Creates a new revision, when called without a specific revision.
	 */
	@Override
    public Result<Page> put(final Page page, String namespace, String key, F.Option<Long> revision) {
		
        // Fix namespace and key just to make sure
        page.namespace = namespace;
        page.key = key;
        
		if (revision.isEmpty()) {
            // Update or create page
            update(page);
            // Get updated page
			return get(namespace, key, revision);	
		}  else {
			// Updating existing revision is a bad idea
			throw new UnsupportedOperationException("Cannot update existing revision");
		}				

	}

	/**
	 * Returns a contents of the page
	 */
    @Override
    public Result<Page> get(String namespace, String key, F.Option<Long> revision) {

		// Lookup the page
		PageEntity page_ = PageEntity.find.where()
                .eq("namespace", namespace)
                .eq("key", key)
                .findUnique();
        
        if (null == page_) {
			// Return stub of a new page
			Page stub = new Page();
			stub.namespace = namespace;
			stub.key = key;
			return NotFound(stub);
        }

		// Find revision
		RevisionEntity revision_ = page_.revision;

		// If specific revision requested
		if (revision.isDefined()) {
			// Find by revision id
			revision_ = RevisionEntity.find.byId(revision.get());
            if (null == revision_) {
                // Specific revision is no found
                return NotFound(null);
            }
		}
		
        // Find meta
        MetaEntity meta_ = page_.meta;

		// Convert to DTO and wrap into Result
		return Ok(fromEntity(page_, revision_, meta_));
        
    }


    /**
     * Convert database entities to DTO
     * @param page
     * @param revision
     * @param meta
     * @return
     */
    private static Page fromEntity(PageEntity page, RevisionEntity revision, MetaEntity meta) {
    	
    	Page page_ = new Page();    	
    	
        page_.namespace = page.namespace;
		page_.key = page.key;
		page_.access = Access.valueOf(page.access);
		page_.state = State.valueOf(page.state);
		page_.template = page.template;
		
        page_.summary = meta.summary;
		page_.thumb = meta.thumb;
		page_.description = meta.description;
		page_.keywords = meta.keywords;
		page_.category = meta.category;
		
        page_.revision = revision.id;
		page_.title = revision.title;
		page_.source = revision.source;
		page_.content = revision.content;
		
        return page_;
        
    }

    /**
     * Create database records from DTO
     * @param page
     * @return
     */
    private static void create(final Page page) {

        final PageEntity page_ = new PageEntity();
        page_.namespace = page.namespace;
        page_.key = page.key;
        page_.access = page.access.name();
        page_.state = page.state.name();
        page_.template = page.template;

        final MetaEntity meta_ = new MetaEntity();
        meta_.page = page_;
        meta_.summary = page.summary;
        meta_.thumb = page.thumb;
        meta_.description = page.description;
        meta_.keywords = page.keywords;
        meta_.category = page.category;
                
        final RevisionEntity revision_ = new RevisionEntity();
        revision_.page = page_;
        revision_.title = page.title;
        revision_.source = page.source;
        revision_.content = page.content;
        
        // Set reverse links
        page_.meta = meta_;
        page_.revision = revision_;
        
        // Transactional save
        txSave(page_, meta_, revision_);
        
    }
    
    private static void update(final Page page) {

        // Lookup page 
        final PageEntity page_ = PageEntity.find.where()
                .eq("namespace", page.namespace)
                .eq("key", page.key)
                .findUnique();
        
        // Create if not exists
        if (null == page_) {
                create(page);
        } else {
       
	        // Update page properties
	        page_.access = page.access.name();
	        page_.state = page.state.name();
	        page_.template = page.template;
	        
	        // Update meta information
	        final MetaEntity meta_ = page_.meta;
	        meta_.summary = page.summary;
	        meta_.thumb = page.thumb;
	        meta_.description = page.description;
	        meta_.keywords = page.keywords;
	        meta_.category = page.category;
        

	        // Check if something changed in content
	        RevisionEntity r = page_.revision;
	        if (r.title.equals(page.title) && r.source.equals(page.source) && r.content.equals(page.content)) {
	            // Update without making new revision
	            txSave(page_, meta_, page_.revision);
	        } else {
	            // Create new revision
	            final RevisionEntity revision_ = new RevisionEntity();
	            revision_.page = page_;
	            revision_.title = page.title;
	            revision_.source = page.source;
	            revision_.content = page.content;
	            // Set reverse link
	            page_.revision = revision_;
	            // Transactional save
	            txSave(page_, meta_, revision_);
	        }
        }
        
        
    }

    /**
     * Transactional save
     * @param page
     * @param meta
     * @param revision
     */
    private static void txSave(final PageEntity page, final MetaEntity meta, final RevisionEntity revision) {
        Ebean.execute(new TxRunnable() {
            @Override
            public void run() {
                page.save();
                meta.save();
                revision.save();
            }
        });    
    }

}
