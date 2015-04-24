package com.stys.platform.pages.impl.repositories;

import com.stys.platform.pages.impl.domain.Selector;
import com.stys.platform.pages.impl.models.*;
import org.joda.time.DateTime;
import play.Application;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxRunnable;
import com.stys.platform.pages.Result;
import com.stys.platform.pages.Results;
import com.stys.platform.pages.Service;
import com.stys.platform.pages.impl.domain.Access;
import com.stys.platform.pages.impl.domain.Page;
import com.stys.platform.pages.impl.domain.State;

import java.sql.Timestamp;

/**
 * Implementation of repository
 */
public class DefaultPagesRepository extends Results implements Service<Result<Page>, Selector, Page> {

	/**
	 * Constructs new instance
	 */
	public DefaultPagesRepository(Application application, Service<Result<Page>, Selector, Page> delegate) {
		/* Empty */
	}
	
	/**
	 * Creates a new revision, when called without a specific revision.
	 */
	@Override
    public Result<Page> put(Selector selector, Page page) {
		
        // Fix namespace and key just to make sure
        page.namespace = selector.namespace;
        page.key = selector.key;
        
		if (selector.revision.isEmpty()) {
            
            // Update or create page
            update(page);
            // Get updated page
			return get(selector);
		
        }  else {
		
			// Updating existing revision is a bad idea
			throw new UnsupportedOperationException("Cannot update existing revision");
	
		}

	}

	/**
	 * Returns a page DTO
	 */
    @Override
    public Result<Page> get(Selector selector) {

		// Lookup the page, join dependent entities
		PageEntity page_ = PageEntity.find
                .fetch("meta")
                .fetch("revision")
                .fetch("access")
                .fetch("state")
                .where()
                .eq("namespace", selector.namespace)
                .eq("key", selector.key)
                .findUnique();
        
        if (null == page_) {
			// Return stub of a new page
			Page stub = new Page();
			stub.namespace = selector.namespace;
			stub.key = selector.key;
			return NotFound(stub);
        }

		// Find revision
		RevisionEntity revision_ = page_.revision;

		// If specific revision requested
		if (selector.revision.isDefined()) {
			// Find by revision id
			revision_ = RevisionEntity.find.byId(selector.revision.get());
            if (null == revision_) {
                // Specific revision is no found
                return NotFound(null);
            }
		}
		
        // Find meta
        MetaEntity meta_ = page_.meta;

        // Find access 
        AccessEntity access_ = page_.access;
        
        // Find state
        StateEntity state_ = page_.state;
        
		// Convert to DTO and wrap into Result
		return Ok(fromEntity(page_, meta_, revision_, access_, state_));
        
    }


    /**
     * Convert database entities to page DTO
     */
    private static Page fromEntity(PageEntity page, MetaEntity meta, RevisionEntity revision, AccessEntity access, StateEntity state) {
    	
    	Page page_ = new Page();
        page_.namespace = page.namespace;
		page_.key = page.key;

        page_.title = meta.title;
        page_.template = meta.template;
		page_.summary = meta.summary;
		page_.thumb = meta.thumb;
		page_.description = meta.description;
		page_.keywords = meta.keywords;
		page_.category = meta.category;
		page_.published = new DateTime(meta.published);
        page_.edited = new DateTime(meta.edited);
        
        page_.revision = revision.id;
		page_.source = revision.source;
		page_.content = revision.content;

        page_.access = Access.valueOf(access.access);
        page_.state = State.valueOf(state.state);
        
        return page_;
        
    }

    /**
     * Creates database records from page DTO
     */
    private static void create(final Page page) {

        final PageEntity page_ = new PageEntity();
        page_.namespace = page.namespace;
        page_.key = page.key;

        final MetaEntity meta_ = new MetaEntity();
        meta_.page = page_;
        meta_.title = page.title;
        meta_.template = page.template;
        meta_.summary = page.summary;
        meta_.thumb = page.thumb;
        meta_.description = page.description;
        meta_.keywords = page.keywords;
        meta_.category = page.category;
                
        final RevisionEntity revision_ = new RevisionEntity();
        revision_.page = page_;
        revision_.source = page.source;
        revision_.content = page.content;
        //revision_.createDateTime = new Timestamp(System.currentTimeMillis());
        
        final AccessEntity access_ = new AccessEntity();
        access_.page = page_;
        access_.access = page.access.name();
        
        final StateEntity state_ = new StateEntity();
        state_.page = page_;
        state_.state = page.state.name();
        if (page.state.equals(State.Published)) {
            meta_.published = new Timestamp(System.currentTimeMillis());
        }
        
        // Set reverse links
        page_.meta = meta_;
        page_.revision = revision_;
        page_.access = access_;
        page_.state = state_;
        
        // Transactional save
        txSave(page_, meta_, revision_, access_, state_);
        
    }

    /**
     * Updates database records from page DTO
     */
    private static void update(final Page page) {

        // Lookup page 
        final PageEntity page_ = PageEntity.find.where()
                .eq("namespace", page.namespace)
                .eq("key", page.key)
                .findUnique();
        
        // Create if not exists
        if (null == page_) {
            create(page);
            return;
        } 
        
        // Update meta information
        final MetaEntity meta_ = page_.meta;
        meta_.title = page.title;
        meta_.template = page.template;
        meta_.summary = page.summary;
        meta_.thumb = page.thumb;
        meta_.description = page.description;
        meta_.keywords = page.keywords;
        meta_.category = page.category;
        
        // Update revision 
        RevisionEntity revision_ = page_.revision;
        // Create new revision if something changed
        if ( !revision_.source.equals(page.source) || !revision_.content.equals(page.content) ) {
            revision_ = new RevisionEntity();
            revision_.page = page_;
            revision_.source = page.source;
            revision_.content = page.content;
            page_.revision = revision_;
            meta_.edited = new Timestamp(System.currentTimeMillis());
        }
        
        // Update access
        AccessEntity access_ = page_.access;
        // Create new access record if changed
        if(!access_.access.equals(page.access.name())) {
            access_ = new AccessEntity();
            access_.access = page.access.name();
            page_.access = access_;
            access_.page = page_;
        }
        
        // Update state
        StateEntity state_ = page_.state;
        // Create new access record if changed
        if(!state_.state.equals(page.state.name())) {
            state_ = new StateEntity();
            state_.state = page.state.name();
            if (page.state.equals(State.Published)) {
                meta_.published = new Timestamp(System.currentTimeMillis());
            }
            page_.state = state_;
            state_.page = page_;
        }
        
        // Transactional save
        txSave(page_, meta_, revision_, access_, state_);
        
    }

    /**
     * Transactional save database entities
     */
    private static void txSave(
            final PageEntity page, final MetaEntity meta, final RevisionEntity revision,
            final AccessEntity access, final StateEntity state) {
        Ebean.execute(new TxRunnable() {
            @Override
            public void run() {
                page.save();
                meta.save();
                revision.save();
                access.save();
                state.save();
            }
        });    
    }

}
