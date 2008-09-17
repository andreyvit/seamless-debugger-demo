/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.ui;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.folding.IFoldingStructureProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.ui.IEditorInput;

import com.yoursway.hello.core.HelloLanguage;

public class HelloEditor extends ScriptEditor {
    
    public static final String EDITOR_ID = "com.yoursway.hello.ui.Editor";
    
    public static final String EDITOR_CONTEXT = "#HelloEditorContext";
    
    public static final String RULER_CONTEXT = "#HelloRulerContext";
    
    @Override
    protected void initializeEditor() {
        super.initializeEditor();
        setEditorContextMenuId(EDITOR_CONTEXT);
        setRulerContextMenuId(RULER_CONTEXT);
    }
    
    @Override
    protected IPreferenceStore getScriptPreferenceStore() {
        return HelloUIPlugin.getDefault().getPreferenceStore();
    }
    
    @Override
    public ScriptTextTools getTextTools() {
        return HelloUIPlugin.getDefault().getTextTools();
    }
    
    @Override
    protected void connectPartitioningToElement(IEditorInput input, IDocument document) {
        if (document instanceof IDocumentExtension3) {
            IDocumentExtension3 extension = (IDocumentExtension3) document;
            if (extension.getDocumentPartitioner(HelloPartitions.HELLO_PARTITIONING) == null) {
                HelloDocumentSetupParticipant participant = new HelloDocumentSetupParticipant();
                participant.setup(document);
            }
        }
    }
    
    @Override
    protected IFoldingStructureProvider getFoldingStructureProvider() {
        return null;
    }
    
    @Override
    public String getEditorId() {
        return EDITOR_ID;
    }
    
    @Override
    public IDLTKLanguageToolkit getLanguageToolkit() {
        return HelloLanguage.getDefault();
    }
    
    @Override
    public String getCallHierarchyID() {
        return "org.eclipse.dltk.callhierarchy.view";
    }
    
    @Override
    protected void initializeKeyBindingScopes() {
        setKeyBindingScopes(new String[] { "com.yoursway.hello.ui.helloEditorContext" }); //$NON-NLS-1$
    }
    
}
