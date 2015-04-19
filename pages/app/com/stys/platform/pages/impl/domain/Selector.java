package com.stys.platform.pages.impl.domain;

import play.libs.F;

/**
 * Selector by namespace, key and optional revision
 */
public class Selector {
    
    public String namespace;
    
    public String key;
    
    public F.Option<Long> revision;
    
    public Selector(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
        this.revision = new F.None<>();
    }
    
    public Selector(String namespace, String key, Long revision) {
        this.namespace = namespace;
        this.key = key;
        this.revision = F.Some(revision);
    }
       
    
}
