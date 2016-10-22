package com.stys.platform.pages.api;

import java.util.Optional;

/** Selector by namespace, key and optional revision */
public class Selector {
    
    public String namespace;
    
    public String key;
    
    public Optional<Long> revision;
    
    public Selector(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
        this.revision = Optional.empty();
    }
    
    public Selector(String namespace, String key, Long revision) {
        this.namespace = namespace;
        this.key = key;
        this.revision = Optional.of(revision);
    }
       
    
}
