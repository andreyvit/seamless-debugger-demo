/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.launching.internal;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterRunner;

import com.yoursway.hello.core.HelloNature;

public class HelloGenericInstall extends AbstractInterpreterInstall {

	public HelloGenericInstall(IInterpreterInstallType type, String id) {
		super(type, id);
	}
	public IInterpreterRunner getInterpreterRunner(String mode) {
		if (mode.equals(ILaunchManager.RUN_MODE)) {
			return new HelloInterpreterRunner(this);
		}
		return super.getInterpreterRunner(mode);
	}

	public String getNatureId() {
		return HelloNature.NATURE_ID;
	}

}