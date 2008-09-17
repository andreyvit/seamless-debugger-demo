/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/

package com.yoursway.hello.debug;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.launching.DebuggingEngineRunner;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.debug.DbgpInterpreterConfig;

public class HelloBasicDebuggerRunner extends DebuggingEngineRunner {
    public static final String ENGINE_ID = "com.yoursway.hello.debug.engine"; //$NON-NLS-1$
    
    private static final String HELLO_HOST_VAR = "DBGP_HELLO_HOST"; //$NON-NLS-1$
    private static final String HELLO_PORT_VAR = "DBGP_HELLO_PORT"; //$NON-NLS-1$
    private static final String HELLO_KEY_VAR = "DBGP_HELLO_KEY"; //$NON-NLS-1$
    private static final String HELLO_LOG_VAR = "DBGP_HELLO_LOG"; //$NON-NLS-1$
    
    private static final String DEBUGGER_SCRIPT = "runner.hello"; //$NON-NLS-1$
    
    protected IPath deploy() throws CoreException {
        //        try {
        //            return RubyBasicDebuggerPlugin.getDefault().deployDebuggerSource();
        //        } catch (IOException e) {
        //            abort(Messages.RubyBasicDebuggerRunner_unableToDeployDebuggerSource, e);
        //        }
        
        return null;
    }
    
    public HelloBasicDebuggerRunner(IInterpreterInstall install) {
        super(install);
    }
    
    @Override
    protected InterpreterConfig addEngineConfig(InterpreterConfig config, PreferencesLookupDelegate delegate)
            throws CoreException {
        // Get debugger source location
        final IPath sourceLocation = deploy();
        
        final IPath scriptFile = sourceLocation.append(DEBUGGER_SCRIPT);
        
        // Creating new config
        InterpreterConfig newConfig = (InterpreterConfig) config.clone();
        
        //        if (getInstall().getInterpreterInstallType() instanceof JRubyInstallType) {
        //            newConfig.addEnvVar("JAVA_OPTS", "-Djruby.jit.enabled=false"); //$NON-NLS-1$ //$NON-NLS-2$
        //            newConfig.addInterpreterArg("-X-C"); //$NON-NLS-1$
        //        }
        //        
        newConfig.addInterpreterArg("-r" + scriptFile.toPortableString()); //$NON-NLS-1$
        newConfig.addInterpreterArg("-I" + sourceLocation.toPortableString()); //$NON-NLS-1$
        
        // Environment
        final DbgpInterpreterConfig dbgpConfig = new DbgpInterpreterConfig(config);
        
        String sessionId = dbgpConfig.getSessionId();
        
        newConfig.addEnvVar(HELLO_HOST_VAR, dbgpConfig.getHost());
        newConfig.addEnvVar(HELLO_PORT_VAR, Integer.toString(dbgpConfig.getPort()));
        newConfig.addEnvVar(HELLO_KEY_VAR, sessionId);
        
        if (isLoggingEnabled(delegate)) {
            newConfig.addEnvVar(HELLO_LOG_VAR, getLogFileName(delegate, sessionId).getAbsolutePath());
        }
        
        return newConfig;
    }
    
    @Override
    protected String getDebuggingEngineId() {
        return ENGINE_ID;
    }
    
    /*
     * @see
     * org.eclipse.dltk.launching.DebuggingEngineRunner#getDebugPreferenceQualifier
     * ()
     */
    @Override
    protected String getDebugPreferenceQualifier() {
        return HelloDebugPlugin.PLUGIN_ID;
    }
    
    /*
     * @seeorg.eclipse.dltk.launching.DebuggingEngineRunner#
     * getDebuggingEnginePreferenceQualifier()
     */
    @Override
    protected String getDebuggingEnginePreferenceQualifier() {
        return "com.yoursway.hello.debug.ui";
        //return HelloDebuggerPlugin.PLUGIN_ID;
    }
    
    /*
     * @seeorg.eclipse.dltk.launching.DebuggingEngineRunner#
     * getLoggingEnabledPreferenceKey()
     */
    @Override
    protected String getLoggingEnabledPreferenceKey() {
        return HelloDebugConstants.ENABLE_LOGGING;
    }
    
    /*
     * @see
     * org.eclipse.dltk.launching.DebuggingEngineRunner#getLogFileNamePreferenceKey
     * ()
     */
    @Override
    protected String getLogFileNamePreferenceKey() {
        return HelloDebugConstants.LOG_FILE_NAME;
    }
    
    /*
     * @see
     * org.eclipse.dltk.launching.DebuggingEngineRunner#getLogFilePathPreferenceKey
     * ()
     */
    @Override
    protected String getLogFilePathPreferenceKey() {
        return HelloDebugConstants.LOG_FILE_PATH;
    }
}
