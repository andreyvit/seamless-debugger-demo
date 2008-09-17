package com.yoursway.hello.core.util;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayListMultiMap<K, V> extends MultiMap<K, V> {
    
    @Override
    protected Collection<V> createInnerCollection() {
        return new ArrayList<V>();
    }
    
}
