package com.yoursway.hello.debug.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.yoursway.hello.debug.ui.internal.HelloDebugUIPlugin;

/**
 * Preference initializer for the 'basic' ruby debugger
 */
public class HelloBasicDebuggerPreferenceInitializer extends
		AbstractPreferenceInitializer {

	/*
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = HelloDebugUIPlugin.getDefault()
				.getPreferenceStore();

		HelloBasicDebuggerConstants.initializeDefaults(store);
	}
}
