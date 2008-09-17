/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.debug.ui.internal.interpreters;

import org.eclipse.dltk.debug.ui.launchConfigurations.InterpreterTab;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterComboBlock;

import com.yoursway.hello.core.HelloNature;

public class HelloInterpreterTab extends InterpreterTab {
	protected AbstractInterpreterComboBlock getInterpreterBlock() {
		return new HelloInterpreterComboBlock();
	}

	protected String getNature() {
		return HelloNature.NATURE_ID;
	}
}
