/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.launching.internal;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.dltk.launching.AbstractInterpreterRunner;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;

public class HelloInterpreterRunner extends AbstractInterpreterRunner {
	public HelloInterpreterRunner(IInterpreterInstall install) {
		super(install);
	}

	protected String getProcessType() {
		return HelloLaunchConfigurationConstants.ID_HELLO_PROCESS_TYPE;
	}
	
	@Override
	protected void alterConfig(ILaunch launch, InterpreterConfig config) {
		super.alterConfig(launch, config);
	}
	
}
