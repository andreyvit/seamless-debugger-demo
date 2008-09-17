package com.yoursway.hello.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class HelloUIPlugin extends AbstractUIPlugin {
    
    // The plug-in ID
    public static final String PLUGIN_ID = "com.yoursway.hello.ui";
    
    // The shared instance
    private static HelloUIPlugin plugin;
    
    private HelloTextTools fHelloTextTools;
    
    /**
     * The constructor
     */
    public HelloUIPlugin() {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
    
    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static HelloUIPlugin getDefault() {
        return plugin;
    }
    
    public HelloTextTools getTextTools() {
        if (fHelloTextTools == null) {
            fHelloTextTools = new HelloTextTools(true);
        }
        return fHelloTextTools;
    }
    
}
