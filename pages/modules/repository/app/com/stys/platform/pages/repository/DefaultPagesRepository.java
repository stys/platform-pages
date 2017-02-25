package com.stys.platform.pages.repository;

import com.avaje.ebean.Ebean;
import com.stys.platform.pages.api.*;
import com.stys.platform.pages.repository.models.*;
import org.joda.time.DateTime;

import static com.stys.platform.pages.api.Result.Status.BadRequest;
import static com.stys.platform.pages.api.Result.Status.NotFound;
import static com.stys.platform.pages.api.Result.Status.Ok;
import static com.stys.platform.pages.api.State.Published;

/** Implementation of repository */
public class DefaultPagesRepository implements Service<Result<Page>, Selector, Page> {
	
	/** Creates a new revision, when called without a specific revision */
	@Override
    public Result<Page> put(Selector selector, Page page) {
		
        // Fix namespace and key just to make sure
        page.namespace = selector.namespace;
        page.key = selector.key;
        
        // Namespace must be set
        if (page.namespace == null || page.namespace.isEmpty()) {
            return Result.of(BadRequest, null);
        }
        
        // Key must be set
        if (page.key == null || page.key.isEmpty()) {
            return Result.of(BadRequest, null);
        }
        
        // Update by creating new revision
		if (null == selector.revision) {
            // Update or create page
            update(page);
            // Get updated page
			return get(selector);
        }  else {
			// Updating existing revision is a bad idea
			throw new UnsupportedOperationException();
		}

	}

	/** Returns a page DTO */
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
			return Result.of(NotFound, stub);
        }

		// Find revision
		RevisionEntity revision_ = page_.revision;

		// If specific revision requested
		if (null != selector.revision) {
			// Find by revision id
			revision_ = RevisionEntity.find.byId(selector.revision);
            if (null == revision_) {
                // Specific revision is no found
                return Result.of(NotFound, null);
            }
		}
		
        // Find meta
        MetaEntity meta_ = page_.meta;

        // Find access 
        AccessEntity access_ = page_.access;
        
        // Find state
        StateEntity state_ = page_.state;
        
		// Convert to DTO and wrap into Result
		return Result.of(Ok, fromEntity(page_, meta_, revision_, access_, state_));
        
    }

    /** Convert database entities to page DTO */
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
		page_.published = state.state.equals(Published.name()) ? new DateTime(state.createDateTime) : null;
        page_.edited = new DateTime(revision.createDateTime);
        
        page_.revision = revision.id;
		page_.source = revision.source;
		page_.content = revision.content;

        page_.access = Access.valueOf(access.access);
        page_.state = State.valueOf(state.state);
        
        return page_;
    }

    /** Creates page entity from page domain object */
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
        
        final AccessEntity access_ = new AccessEntity();
        access_.page = page_;
        access_.access = page.access.name();
        
        final StateEntity state_ = new StateEntity();
        state_.page = page_;
        state_.state = page.state.name();
        
        // Set reverse links
        page_.meta = meta_;
        page_.revision = revision_;
        page_.access = access_;
        page_.state = state_;
        
        // Transactional save
        txSave(page_, meta_, revision_, access_, state_);
    }

    /** Updates database records from page DTO */
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
            page_.state = state_;
            state_.page = page_;
        }
        
        // Transactional save
        txSave(page_, meta_, revision_, access_, state_);
    }

    /** Transactional save database entities */
    private static void txSave(
            final PageEntity page, final MetaEntity meta, final RevisionEntity revision,
            final AccessEntity access, final StateEntity state) {
        Ebean.execute(() -> {
            page.save();
            meta.save();
            revision.save();
            access.save();
            state.save();
        });
    }

}
