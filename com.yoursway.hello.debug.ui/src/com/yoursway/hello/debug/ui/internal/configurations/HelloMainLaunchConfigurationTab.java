/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.debug.ui.internal.configurations;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.debug.ui.launchConfigurations.MainLaunchConfigurationTab;
import org.eclipse.swt.widgets.Composite;

import com.yoursway.hello.core.HelloLanguage;
import com.yoursway.hello.core.HelloNature;

public class HelloMainLaunchConfigurationTab extends MainLaunchConfigurationTab {

	protected String getNatureID() {
		return HelloNature.NATURE_ID;
	}

	protected boolean isValidToolkit(IDLTKLanguageToolkit toolkit) {
		return (toolkit instanceof HelloLanguage) ? true : false;
	}

	@Override
	protected boolean breakOnFirstLinePrefEnabled(
			PreferencesLookupDelegate delegate) {
		return false;
	}

	@Override
	protected boolean dbpgLoggingPrefEnabled(PreferencesLookupDelegate delegate) {
		return false;
	}

	@Override
	protected void createDebugOptionsGroup(Composite parent) {
	}

}
