package com.yoursway.hello.debug;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.debug.core.DLTKDebugPreferenceConstants;

public class HelloDebugPreferenceInitializer extends AbstractPreferenceInitializer {
    
    @Override
    public void initializeDefaultPreferences() {
        Preferences store = HelloDebugPlugin.getDefault().getPluginPreferences();
        
        if (store.getDefaultString(HelloDebugConstants.DEBUGGING_ENGINE_ID_KEY) == null)
            store.setDefault(HelloDebugConstants.DEBUGGING_ENGINE_ID_KEY, "com.yoursway.hello.debugger"); //$NON-NLS-1$
            
        store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_BREAK_ON_FIRST_LINE, false);
        store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_ENABLE_LOGGING, false);
        
        store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_SHOW_SCOPE_GLOBAL, true);
        store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_SHOW_SCOPE_CLASS, true);
        store.setDefault(DLTKDebugPreferenceConstants.PREF_DBGP_SHOW_SCOPE_LOCAL, true);
    }
    
}
