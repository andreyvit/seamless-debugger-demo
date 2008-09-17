package com.yoursway.hello.debug;

import org.eclipse.dltk.core.DLTKIdContributionSelector;
import org.eclipse.dltk.core.PreferencesLookupDelegate;

public class HelloDebuggingEngineSelector extends DLTKIdContributionSelector {
    
    /*
     * @see
     * org.eclipse.dltk.core.DLTKIdContributionSelector#getSavedContributionId
     * (org.eclipse.dltk.core.PreferencesLookupDelegate)
     */
    @Override
    protected String getSavedContributionId(PreferencesLookupDelegate delegate) {
        return delegate.getString(HelloDebugPlugin.PLUGIN_ID, HelloDebugConstants.DEBUGGING_ENGINE_ID_KEY);
    }
}
