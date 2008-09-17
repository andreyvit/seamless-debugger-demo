package com.yoursway.hello.launching.internal;

import java.net.URI;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.dltk.debug.core.model.IScriptDebugTarget;
import org.eclipse.dltk.debug.core.model.IScriptStack;
import org.eclipse.dltk.debug.core.model.IScriptStackFrame;
import org.eclipse.dltk.debug.core.model.IScriptThread;
import org.eclipse.dltk.debug.core.model.IScriptVariable;
import org.eclipse.dltk.internal.debug.core.model.ScriptStackFrame;
import org.eclipse.dltk.internal.debug.core.model.ScriptThread;

public class ScriptProxyFrame extends ScriptStackFrame {

	private final ScriptProxyThread thread;
	private final ScriptStackFrame frame;

	public IScriptVariable findVariable(String varName) throws DebugException {
		return frame.findVariable(varName);
	}

	public URI getFileName() {
		return frame.getFileName();
	}

	public int getLevel() {
		return frame.getLevel();
	}

	public IScriptDebugTarget getScriptDebugTarget() {
		return frame.getScriptDebugTarget();
	}

	public IScriptThread getScriptThread() {
		return frame.getScriptThread();
	}

	public String getSourceLine() {
		return frame.getSourceLine();
	}

	public URI getSourceURI() {
		return frame.getSourceURI();
	}

	public IScriptStack getStack() {
		return frame.getStack();
	}

	public String toString() {
		return frame.toString();
	}

	public void updateVariables() {
		if (frame == null)
			return;
		frame.updateVariables();
	}

	public boolean canResume() {
		return frame.canResume();
	}

	public boolean canStepInto() {
		return frame.canStepInto();
	}

	public boolean canStepOver() {
		return frame.canStepOver();
	}

	public boolean canStepReturn() {
		return frame.canStepReturn();
	}

	public boolean canSuspend() {
		return frame.canSuspend();
	}

	public boolean canTerminate() {
		return frame.canTerminate();
	}

	public Object getAdapter(Class adapter) {
		return frame.getAdapter(adapter);
	}

	public int getCharEnd() throws DebugException {
		return frame.getCharEnd();
	}

	public int getCharStart() throws DebugException {
		return frame.getCharStart();
	}

	public IDebugTarget getDebugTarget() {
		return thread.getDebugTarget();
	}

	public ILaunch getLaunch() {
		return frame.getLaunch();
	}

	public int getLineNumber() throws DebugException {
		return frame.getLineNumber();
	}

	public String getModelIdentifier() {
		return frame.getModelIdentifier();
	}

	public String getName() throws DebugException {
		return frame.getName();
	}

	public IRegisterGroup[] getRegisterGroups() throws DebugException {
		return frame.getRegisterGroups();
	}

	public IThread getThread() {
		return thread;
	}

	public IVariable[] getVariables() throws DebugException {
		return frame.getVariables();
	}

	public boolean hasRegisterGroups() throws DebugException {
		return frame.hasRegisterGroups();
	}

	public boolean hasVariables() throws DebugException {
		return frame.hasVariables();
	}

	public boolean isStepping() {
		return frame.isStepping();
	}

	public boolean isSuspended() {
		return frame.isSuspended();
	}

	public boolean isTerminated() {
		return frame.isTerminated();
	}

	public void resume() throws DebugException {
		frame.resume();
	}

	public void stepInto() throws DebugException {
		frame.stepInto();
	}

	public void stepOver() throws DebugException {
		frame.stepOver();
	}

	public void stepReturn() throws DebugException {
		frame.stepReturn();
	}

	public void suspend() throws DebugException {
		frame.suspend();
	}

	public void terminate() throws DebugException {
		frame.terminate();
	}

	public ScriptProxyFrame(ScriptProxyThread thread, ScriptStackFrame frame) {
		super(new IScriptStack() {

			public IScriptStackFrame[] getFrames() {
				return null;
			}

			public ScriptThread getThread() {
				return null;
			}

			public IScriptStackFrame getTopFrame() {
				return null;
			}

			public boolean hasFrames() {
				return false;
			}

			public int size() {
				return 0;
			}
			
		}, null);
		this.thread = thread;
		this.frame = frame;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((frame == null) ? 0 : frame.hashCode());
		result = prime * result + ((thread == null) ? 0 : thread.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScriptProxyFrame other = (ScriptProxyFrame) obj;
		if (frame == null) {
			if (other.frame != null)
				return false;
		} else if (!frame.equals(other.frame))
			return false;
		if (thread == null) {
			if (other.thread != null)
				return false;
		} else if (!thread.equals(other.thread))
			return false;
		return true;
	}
	
}
