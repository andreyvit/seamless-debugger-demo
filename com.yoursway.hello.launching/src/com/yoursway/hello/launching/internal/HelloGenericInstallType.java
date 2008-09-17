/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.launching.internal;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.internal.launching.InterpreterMessages;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.LibraryLocation;

import com.yoursway.hello.core.HelloNature;

public class HelloGenericInstallType extends AbstractInterpreterInstallType {

	private static final String INSTALL_TYPE_NAME = "Hello Interpreter";


	public String getNatureId() {
		return HelloNature.NATURE_ID;
	}

	public String getName() {
		return INSTALL_TYPE_NAME;
	}

	protected String getPluginId() {
		return HelloLaunchingPlugin.PLUGIN_ID;
	}

	protected IInterpreterInstall doCreateInterpreterInstall(String id) {
		return new HelloGenericInstall(this, id);
	}

	protected ILog getLog() {
		return HelloLaunchingPlugin.getDefault().getLog();
	}
	
	@Override
	public synchronized LibraryLocation[] getDefaultLibraryLocations(
			final File installLocation, EnvironmentVariable[] variables,
			IProgressMonitor monitor) {
		return new LibraryLocation[0];
	}

	@Override
	protected File createPathFile() throws IOException {
		// Pizdec, blyat'
		throw new RuntimeException("OMG, how did I get here?!");
	}
	
	@Override
	protected String[] getPossibleInterpreterNames() {
		// Should never come here because we override validateInstallLocation()
		throw new RuntimeException("OMG, how did I get here?!");
	}

	@Override
	public IStatus validateInstallLocation(File installLocation) {
		if (!installLocation.exists() || !installLocation.isFile()
				|| installLocation.isHidden()) {
			return createStatus(IStatus.ERROR,
					InterpreterMessages.errNonExistentOrInvalidInstallLocation,
					null);
		}

		if (Platform.getOS().equals(Platform.OS_WIN32) && !installLocation.getName().endsWith(".exe")) {
			return createStatus(IStatus.ERROR,
					InterpreterMessages.errNoInterpreterExecutablesFound, null);
		}

		return createStatus(IStatus.OK, "", null); //$NON-NLS-1$
	}
}
