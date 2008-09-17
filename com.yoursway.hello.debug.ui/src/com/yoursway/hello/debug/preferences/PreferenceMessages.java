package com.yoursway.hello.debug.preferences;

import org.eclipse.osgi.util.NLS;

public class PreferenceMessages {
    private static final String BUNDLE_NAME = "com.yoursway.hello.debug.PreferenceMessages"; //$NON-NLS-1$
    
    static {
        NLS.initializeMessages(BUNDLE_NAME, PreferenceMessages.class);
    }
    
    public static String PreferencesDescription;
    public static String NoSettingsAvailable;
}
