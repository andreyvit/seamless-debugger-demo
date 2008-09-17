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
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ui.IDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.ScriptElementLabels;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.viewsupport.ScriptUILabelProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;

import com.yoursway.hello.core.HelloLanguage;

public class HelloUILanguageToolkit implements IDLTKUILanguageToolkit {
    
    private static HelloUILanguageToolkit sToolkit = null;
    
    public static IDLTKUILanguageToolkit getInstance() {
        if (sToolkit == null)
            sToolkit = new HelloUILanguageToolkit();
        return sToolkit;
    }
    
    public IPreferenceStore getPreferenceStore() {
        return HelloUIPlugin.getDefault().getPreferenceStore();
    }
    
    public IDLTKLanguageToolkit getCoreToolkit() {
        return HelloLanguage.getDefault();
    }
    
    public IDialogSettings getDialogSettings() {
        return HelloUIPlugin.getDefault().getDialogSettings();
    }
    
    public String getPartitioningId() {
        return HelloPartitions.HELLO_PARTITIONING;
    }
    
    public String getEditorId(Object inputElement) {
        return HelloEditor.EDITOR_ID;
    }
    
    public String getInterpreterContainerId() {
        return "com.yoursway.hello.launching.INTERPRETER_CONTAINER";
    }
    
    public ScriptUILabelProvider createScriptUILabelProvider() {
        return null;
    }
    
    public boolean getProvideMembers(ISourceModule element) {
        return true;
    }
    
    public ScriptTextTools getTextTools() {
        return HelloUIPlugin.getDefault().getTextTools();
    }
    
    public ScriptSourceViewerConfiguration createSourceViewerConfiguration() {
        return new SimpleHelloSourceViewerConfiguration(getTextTools().getColorManager(),
                getPreferenceStore(), null, getPartitioningId(), false);
    }
    
    private static final String INTERPRETERS_PREFERENCE_PAGE_ID = "com.yoursway.hello.preferences.interpreters";
    private static final String DEBUG_PREFERENCE_PAGE_ID = null;//"org.eclipse.dltk.ruby.preferences.debug";
    
    public String getInterpreterPreferencePage() {
        return INTERPRETERS_PREFERENCE_PAGE_ID;
    }
    
    public String getDebugPreferencePage() {
        return DEBUG_PREFERENCE_PAGE_ID;
    }
    
    public String[] getEditorPreferencePages() {
        return new String[0];
    }
    
    public ScriptElementLabels getScriptElementLabels() {
        return null;
    }
    
}
