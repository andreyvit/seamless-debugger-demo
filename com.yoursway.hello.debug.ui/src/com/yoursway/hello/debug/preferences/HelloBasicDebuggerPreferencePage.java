/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package com.yoursway.hello.debug.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.debug.ui.preferences.DebuggingEngineConfigOptionsBlock;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

import com.yoursway.hello.debug.HelloDebugConstants;
import com.yoursway.hello.debug.ui.internal.HelloDebugUIPlugin;

public class HelloBasicDebuggerPreferencePage extends
		AbstractConfigurationBlockPropertyAndPreferencePage {

	private static PreferenceKey ENABLE_LOGGING = new PreferenceKey(
			HelloDebugUIPlugin.PLUGIN_ID, HelloDebugConstants.ENABLE_LOGGING);

	private static PreferenceKey LOG_FILE_PATH = new PreferenceKey(
			HelloDebugUIPlugin.PLUGIN_ID, HelloDebugConstants.LOG_FILE_PATH);

	private static PreferenceKey LOG_FILE_NAME = new PreferenceKey(
			HelloDebugUIPlugin.PLUGIN_ID, HelloDebugConstants.LOG_FILE_NAME);

	private static String PREFERENCE_PAGE_ID = "com.yoursway.hello.preferences.debug.engines.basicdebugger"; //$NON-NLS-1$
	private static String PROPERTY_PAGE_ID = "com.yoursway.hello.propertyPage.debug.engines.basicdebugger"; //$NON-NLS-1$

	protected AbstractOptionsBlock createOptionsBlock(
			IStatusChangeListener newStatusChangedListener, IProject project,
			IWorkbenchPreferenceContainer container) {
		return new DebuggingEngineConfigOptionsBlock(newStatusChangedListener,
				project, new PreferenceKey[] { ENABLE_LOGGING, LOG_FILE_PATH,
						LOG_FILE_NAME }, container) {

			protected void createEngineBlock(Composite composite) {
				// no engine preferences, yet...
			}

			protected PreferenceKey getEnableLoggingPreferenceKey() {
				return ENABLE_LOGGING;
			}

			protected PreferenceKey getLogFileNamePreferenceKey() {
				return LOG_FILE_NAME;
			}

			protected PreferenceKey getLogFilePathPreferenceKey() {
				return LOG_FILE_PATH;
			}
		};
	}

	/*
	 * @seeorg.eclipse.dltk.ui.preferences.
	 * AbstractConfigurationBlockPropertyAndPreferencePage#getHelpId()
	 */
	protected String getHelpId() {
		return null;
	}

	/*
	 * @seeorg.eclipse.dltk.internal.ui.preferences.PropertyAndPreferencePage#
	 * getPreferencePageId()
	 */
	protected String getPreferencePageId() {
		return PREFERENCE_PAGE_ID;
	}

	/*
	 * @seeorg.eclipse.dltk.ui.preferences.
	 * AbstractConfigurationBlockPropertyAndPreferencePage#getProjectHelpId()
	 */
	protected String getProjectHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @seeorg.eclipse.dltk.internal.ui.preferences.PropertyAndPreferencePage#
	 * getPropertyPageId()
	 */
	protected String getPropertyPageId() {
		return PROPERTY_PAGE_ID;
	}

	/*
	 * @seeorg.eclipse.dltk.ui.preferences.
	 * AbstractConfigurationBlockPropertyAndPreferencePage#setDescription()
	 */
	protected void setDescription() {
		setDescription(PreferenceMessages.PreferencesDescription);
	}

	/*
	 * @seeorg.eclipse.dltk.ui.preferences.
	 * AbstractConfigurationBlockPropertyAndPreferencePage#setPreferenceStore()
	 */
	protected void setPreferenceStore() {
		setPreferenceStore(HelloDebugUIPlugin.getDefault().getPreferenceStore());
	}
}
