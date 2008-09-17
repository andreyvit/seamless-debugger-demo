/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.ui;

import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlock;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class HelloGlobalPreferencesPage extends AbstractConfigurationBlockPreferencePage {
    
    @Override
    protected IPreferenceConfigurationBlock createConfigurationBlock(
            OverlayPreferenceStore overlayPreferenceStore) {
        return new AbstractConfigurationBlock(overlayPreferenceStore, this) {
            public Control createControl(Composite parent) {
                Composite composite = SWTFactory.createComposite(parent, parent.getFont(), 1, 1,
                        GridData.FILL_HORIZONTAL);
                return composite;
            }
        };
    }
    
    @Override
    protected String getHelpId() {
        return null;
    }
    
    @Override
    protected void setDescription() {
        setDescription("Global Hello Preferences");
    }
    
    @Override
    protected void setPreferenceStore() {
        setPreferenceStore(HelloUIPlugin.getDefault().getPreferenceStore());
    }
}
