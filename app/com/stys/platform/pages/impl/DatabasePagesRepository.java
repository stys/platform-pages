package com.stys.platform.pages.impl;

import play.libs.F;

import com.stys.platform.pages.Repository;
import com.stys.platform.pages.impl.models.Page;
import com.stys.platform.pages.impl.models.Revision;

/**
 * Implementation of show repository over {@link com.stys.platform.pages.impl.models.Revision}.
 */
public class DatabasePagesRepository implements Repository<com.stys.platform.pages.impl.Page> {

	private static final F.Option<com.stys.platform.pages.impl.Page> None = 
			new F.None<com.stys.platform.pages.impl.Page>();
	
	/**
	 * Creates a new revision, when called without a specific revision. Otherwise
	 * overwrites the contents of the specified revision. 
	 */
	@Override
    public void put(com.stys.platform.pages.impl.Page page, String namespace, String key, F.Option<Long> revision) {
		
		if (revision.isEmpty()) {

			// Try to find page entity
			Page entity = Page.find.where()
					.eq("namespace", namespace)
					.eq("key", key)
					.findUnique();

			if (null == entity) {
				// new page
				Page.create(namespace, key, page);

			} else {
				// update template
				entity.status = page.status;
				entity.template = page.template;
				entity.save();
				
				// new revision of existing page
				Revision.create(entity, page.title, page.source, page.content);
			}

		}  else {
			// Updating existing revision is a very bad idea
			throw new UnsupportedOperationException("Cannot update existing revision");
		}				

	}

	/**
	 * Returns a contents of the page
	 */
    @Override
    public F.Option<com.stys.platform.pages.impl.Page> get(String namespace, String key, F.Option<Long> revision) {

		// Lookup the page
		Page entity = Page.find.where()
				.eq("namespace", namespace)
				.eq("key", key)
				.findUnique();

		if (null == entity) {
			return None;
		}

		// Find revision
		Revision rev = null;

		// If specific revision requested
		if (revision.isDefined()) {

			// Find by revision id
			rev = Revision.find.byId(revision.get());

		// Fetch latest revision
		} else {

			rev = Revision.find.where().eq("page", entity).orderBy("revision desc").setMaxRows(1).findUnique();

		}

		if (null == rev) {
			return None;
		}

		// Prepare page
		com.stys.platform.pages.impl.Page page = new com.stys.platform.pages.impl.Page();

		page.namespace = entity.namespace;
		page.key = entity.key;
		page.status = entity.status;
		page.template = entity.template;

		page.revision = rev.revision;
		page.title = rev.title;
		page.source = rev.source;
		page.content = rev.content;

		// Done
		return F.Some(page);
    }
}
