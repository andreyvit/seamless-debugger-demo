/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.debug;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class HelloDebugPlugin extends Plugin {
    
    // The plug-in ID
    public static final String PLUGIN_ID = "com.yoursway.hello.debug"; //$NON-NLS-1$
    
    // The shared instance
    private static HelloDebugPlugin plugin;
    
    /**
     * The constructor
     */
    public HelloDebugPlugin() {
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }
    
    @Override
    public void stop(BundleContext context) throws Exception {
        try {
            savePluginPreferences();
        } finally {
            plugin = null;
            super.stop(context);
        }
    }
    
    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static HelloDebugPlugin getDefault() {
        return plugin;
    }
}
