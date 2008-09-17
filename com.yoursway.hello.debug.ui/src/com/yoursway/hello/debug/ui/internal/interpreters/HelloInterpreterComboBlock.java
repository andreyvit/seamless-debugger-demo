/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.debug.ui.internal.interpreters;

import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterComboBlock;

import com.yoursway.hello.core.HelloNature;
import com.yoursway.hello.debug.HelloLaunchingUtils;

public class HelloInterpreterComboBlock extends AbstractInterpreterComboBlock {
	protected void showInterpreterPreferencePage() {
		showPrefPage(HelloInterpreterPreferencePage.PAGE_ID);
	}

	protected String getCurrentLanguageNature() {
		return HelloNature.NATURE_ID;
	}
	
	protected void fillWithWorkspaceInterpreters() {
		setInterpreters(HelloLaunchingUtils.interpreters());	
	}
	
}
