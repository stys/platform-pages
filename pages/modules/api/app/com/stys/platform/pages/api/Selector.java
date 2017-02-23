package com.stys.platform.pages.api;

/** Selector by namespace, key and optional revision */
public class Selector {
    
    public String namespace;
    
    public String key;
    
    public Long revision;
    
    public Selector(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
        this.revision = null;
    }
    
    public Selector(String namespace, String key, Long revision) {
        this.namespace = namespace;
        this.key = key;
        this.revision = revision;
    }
       
    
}
