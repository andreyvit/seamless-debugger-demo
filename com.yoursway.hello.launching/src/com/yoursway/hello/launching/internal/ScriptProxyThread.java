package com.yoursway.hello.launching.internal;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.dltk.internal.debug.core.model.ScriptStackFrame;

public class ScriptProxyThread implements IThread {

	final IThread thread;
	private final HelloDebugTarget debugTarget;

	public boolean canResume() {
		return thread.canResume();
	}

	public boolean canStepInto() {
		return thread.canStepInto();
	}

	public boolean canStepOver() {
		return thread.canStepOver();
	}

	public boolean canStepReturn() {
		return thread.canStepReturn();
	}

	public boolean canSuspend() {
		return thread.canSuspend();
	}

	public boolean canTerminate() {
		return thread.canTerminate();
	}

	public Object getAdapter(Class adapter) {
		return thread.getAdapter(adapter);
	}

	public IBreakpoint[] getBreakpoints() {
		return thread.getBreakpoints();
	}

	public IDebugTarget getDebugTarget() {
		return debugTarget;
	}

	public ILaunch getLaunch() {
		return thread.getLaunch();
	}

	public String getModelIdentifier() {
		return thread.getModelIdentifier();
	}

	public String getName() throws DebugException {
		return thread.getName();
	}

	public int getPriority() throws DebugException {
		return thread.getPriority();
	}

	public IStackFrame[] getStackFrames() throws DebugException {
		IStackFrame[] originals = thread.getStackFrames();
		ScriptProxyFrame[] frames = new ScriptProxyFrame[originals.length]; 
		for (int i = 0; i < frames.length; i++) {
			IStackFrame original = originals[i];
			frames[i] = new ScriptProxyFrame(this, (ScriptStackFrame) original);
		}
		return frames;
	}

	public IStackFrame getTopStackFrame() throws DebugException {
		IStackFrame top = thread.getTopStackFrame();
		if (top == null)
			return null;
		return new ScriptProxyFrame(this, (ScriptStackFrame) top);
	}

	public boolean hasStackFrames() throws DebugException {
		return thread.hasStackFrames();
	}

	public boolean isStepping() {
		return thread.isStepping();
	}

	public boolean isSuspended() {
		return thread.isSuspended();
	}

	public boolean isTerminated() {
		return thread.isTerminated();
	}

	public void resume() throws DebugException {
		thread.resume();
	}

	public void stepInto() throws DebugException {
		thread.stepInto();
	}

	public void stepOver() throws DebugException {
		thread.stepOver();
	}

	public void stepReturn() throws DebugException {
		thread.stepReturn();
	}

	public void suspend() throws DebugException {
		thread.suspend();
	}

	public void terminate() throws DebugException {
		thread.terminate();
	}

	public ScriptProxyThread(IThread thread, HelloDebugTarget debugTarget) {
		this.thread = thread;
		this.debugTarget = debugTarget;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((thread == null) ? 0 : thread.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScriptProxyThread other = (ScriptProxyThread) obj;
		if (thread == null) {
			if (other.thread != null)
				return false;
		} else if (!thread.equals(other.thread))
			return false;
		return true;
	}

}
