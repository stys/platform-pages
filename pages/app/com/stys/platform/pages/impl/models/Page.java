package com.stys.platform.pages.impl.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxRunnable;

/**
 * Page metadata
 */
@Entity
public class Page extends Model {

    /**
	 * Default serial version id
	 */
	private static final long serialVersionUID = 1L;

	@Id
    public Long id;

    public String namespace;

    public String key;

    public String access;

    public String state;
    
    public String template;
    
    @OneToOne
    public Revision revision;
    
    @OneToMany(mappedBy = "page")
    public List<Revision> revisions;
    
    @OneToOne
    public PageMetaInfo metaInfo;

    public static final Finder<Long, Page> find = new Finder<>(Long.class, Page.class);

    public static Page create(final com.stys.platform.pages.impl.domain.Page page) {

        final Page entity = new Page();
        final Revision revision = new Revision();
        final PageMetaInfo metaInfo = new PageMetaInfo();

        entity.namespace = page.namespace;
        entity.key = page.key;
        entity.access = page.access.name();
        entity.state = page.state.name();
        entity.template = page.template;

        revision.page = entity;
        revision.title = page.title;
        revision.source = page.source;
        revision.content = page.content;
                
        entity.revision = revision;
        entity.metaInfo = metaInfo;
        
        // Transactional save 
        Ebean.execute(new TxRunnable() {
            @Override
            public void run() {
                entity.save();
                revision.save();
                metaInfo.save();
            }
        });

        return entity;
    }

    public static Page create(String namespace, String key, com.stys.platform.pages.impl.domain.Page page) {
        page.namespace = namespace;
        page.key = key;
        return create(page);
    }

}
