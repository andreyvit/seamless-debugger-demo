/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.ui;

import org.eclipse.jface.text.IDocument;

public final class HelloPartitions {
    public static final String HELLO_PARTITIONING = "__hello_partitioning";
    
    public final static String[] HELLO_PARTITION_TYPES = new String[] { IDocument.DEFAULT_CONTENT_TYPE };
    
    private HelloPartitions() {
        
    }
}
