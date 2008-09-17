/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.ui;

import org.eclipse.dltk.internal.ui.editor.semantic.highlighting.PositionUpdater;
import org.eclipse.dltk.internal.ui.editor.semantic.highlighting.SemanticHighlighting;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.ui.texteditor.ITextEditor;

public class HelloTextTools extends ScriptTextTools {
    
    private final IPartitionTokenScanner fPartitionScanner;
    
    private final static String[] LEGAL_CONTENT_TYPES = new String[] {};
    
    public HelloTextTools(boolean autoDisposeOnDisplayDispose) {
        super(HelloPartitions.HELLO_PARTITIONING, LEGAL_CONTENT_TYPES, autoDisposeOnDisplayDispose);
        fPartitionScanner = new HelloPartitionScanner();
    }
    
    @Override
    public ScriptSourceViewerConfiguration createSourceViewerConfiguraton(IPreferenceStore preferenceStore,
            ITextEditor editor, String partitioning) {
        return new HelloSourceViewerConfiguration(getColorManager(), preferenceStore, editor, partitioning);
    }
    
    @Override
    public IPartitionTokenScanner getPartitionScanner() {
        return fPartitionScanner;
    }
    
    @Override
    public SemanticHighlighting[] getSemanticHighlightings() {
        return new SemanticHighlighting[0];
    }
    
    @Override
    public PositionUpdater getSemanticPositionUpdater() {
        return null;
    }
}
