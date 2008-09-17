package com.yoursway.hello.debug;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "com.yoursway.hello.debug.messages"; //$NON-NLS-1$
    public static String RubyBasicDebuggerRunner_unableToDeployDebuggerSource;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
    
    private Messages() {
    }
}
