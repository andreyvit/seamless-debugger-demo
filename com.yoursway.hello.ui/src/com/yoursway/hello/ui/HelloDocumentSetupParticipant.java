/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.ui;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;

/**
 * The document setup participant for Ruby.
 */
public class HelloDocumentSetupParticipant implements IDocumentSetupParticipant {
    
    public HelloDocumentSetupParticipant() {
        
    }
    
    public void setup(IDocument document) {
        HelloTextTools tools = HelloUIPlugin.getDefault().getTextTools();
        tools.setupDocumentPartitioner(document, HelloPartitions.HELLO_PARTITIONING);
    }
}
