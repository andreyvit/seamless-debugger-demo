package com.yoursway.hello.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class HelloCorePlugin extends AbstractUIPlugin {
    
    public static final boolean DUMP_EXCEPTIONS_TO_CONSOLE = Boolean.valueOf(
            Platform.getDebugOption("com.yoursway.hello.core/dumpErrorsToConsole")).booleanValue();
    
    // The plug-in ID
    public static final String PLUGIN_ID = "com.yoursway.hello.core";
    
    // The shared instance
    private static HelloCorePlugin plugin;
    
    /**
     * The constructor
     */
    public HelloCorePlugin() {
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
    public static HelloCorePlugin getDefault() {
        return plugin;
    }
    
    public static void log(Exception ex) {
        if (DLTKCore.DEBUG || DUMP_EXCEPTIONS_TO_CONSOLE)
            ex.printStackTrace(System.err);
        String message = ex.getMessage();
        if (message == null)
            message = "(no message)";
        getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, 0, message, ex));
    }
    
    public static void log(String message) {
        if (DLTKCore.DEBUG || DUMP_EXCEPTIONS_TO_CONSOLE)
            System.err.println(message);
        getDefault().getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, 0, message, null));
    }
    
}
