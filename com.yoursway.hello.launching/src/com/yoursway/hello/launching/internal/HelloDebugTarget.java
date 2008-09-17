/**
 * 
 */
package com.yoursway.hello.launching.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.dltk.debug.core.IDbgpService;
import org.eclipse.dltk.internal.debug.core.model.ScriptDebugTarget;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;

@SuppressWarnings("restriction")
final class HelloDebugTarget extends ScriptDebugTarget {
	
	private JDIDebugTarget javaTarget;

	HelloDebugTarget(String modelId,
			IDbgpService dbgpService, String sessionId, ILaunch launch,
			IProcess process) throws CoreException {
		super(modelId, dbgpService, sessionId, launch, process);
	}
	
	public IThread[] getOriginalThreads() throws DebugException {
		return super.getThreads();
	}
	
	public void setJavaTarget(JDIDebugTarget javaTarget) {
		this.javaTarget = javaTarget;
	}

	@Override
	public IThread[] getThreads() throws DebugException {
		IThread[] threads = super.getThreads();
		IThread[] proxyThreads = new IThread[threads.length];
		for (int i = 0; i < proxyThreads.length; i++) {
			proxyThreads[i] = new ScriptProxyThread(threads[i], this, javaTarget);
		}
		return proxyThreads;
	}
	
}