/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.launching.internal;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;

public final class HelloLaunchConfigurationConstants extends
		ScriptLaunchConfigurationConstants {

	private HelloLaunchConfigurationConstants() {

	}

	public static final String ID_HELLO_SCRIPT = "com.yoursway.hello.launching.HelloLaunchConfigurationType"; //$NON-NLS-1$

	public static final String ID_HELLO_PROCESS_TYPE = "helloInterpreter"; //$NON-NLS-1$

	public static int findInterpreterFrame(IStackFrame[] stackFrames) throws DebugException {
		int candidate = -1;
		for (int i = 0; i < stackFrames.length; i++) {
			JDIStackFrame frame = (JDIStackFrame) stackFrames[i];
			String typeName = frame.getReceivingTypeName();
			if (typeName.startsWith("sun.")
					|| typeName
							.startsWith("java.")) {
				if (candidate == -1)
					candidate = i;
				continue;
			}
			if (typeName
					.startsWith("com.yoursway.hello.interpreter.")) {
				if (candidate == -1)
					return i;
				else
					return candidate;
			}
			candidate = -1;
		}
		return -1;
	}
	
}