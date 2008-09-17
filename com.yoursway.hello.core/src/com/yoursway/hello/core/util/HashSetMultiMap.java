package com.yoursway.hello.core.util;

import java.util.Collection;
import java.util.HashSet;

public class HashSetMultiMap<K,V> extends MultiMap<K, V> {
    
    @Override
    protected Collection<V> createInnerCollection() {
        return new HashSet<V>();
    }
    
}
