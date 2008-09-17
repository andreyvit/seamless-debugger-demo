package com.yoursway.hello.debug.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.yoursway.hello.debug.HelloDebugConstants;


public class HelloBasicDebuggerConstants {
    
    public static void initializeDefaults(IPreferenceStore store) {
        store.setDefault(HelloDebugConstants.ENABLE_LOGGING, false);
        store.setDefault(HelloDebugConstants.LOG_FILE_NAME, "helloDebug_{0}.log"); //$NON-NLS-1$
        store.setDefault(HelloDebugConstants.LOG_FILE_PATH, ""); //$NON-NLS-1$
    }
    
    private HelloBasicDebuggerConstants() {
        // private constructor
    }
}
