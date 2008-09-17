/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.ui;

import org.eclipse.dltk.internal.ui.editor.ScriptSourceViewer;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.ITextEditor;

public class HelloSourceViewerConfiguration extends ScriptSourceViewerConfiguration {
    
    public HelloSourceViewerConfiguration(IColorManager colorManager, IPreferenceStore preferenceStore,
            ITextEditor editor, String partitioning) {
        super(colorManager, preferenceStore, editor, partitioning);
    }
    
    @Override
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return HelloPartitions.HELLO_PARTITION_TYPES;
    }
    
    @Override
    protected ContentAssistPreference getContentAssistPreference() {
        return new ContentAssistPreference() {
            
            @Override
            protected ScriptTextTools getTextTools() {
                return HelloUIPlugin.getDefault().getTextTools();
            }
            
        };
    }
    
    @Override
    public IInformationPresenter getOutlinePresenter(ScriptSourceViewer sourceViewer, boolean doCodeResolve) {
        return null;
    }
}
