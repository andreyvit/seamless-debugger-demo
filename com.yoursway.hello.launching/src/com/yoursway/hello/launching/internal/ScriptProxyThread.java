package com.yoursway.hello.launching.internal;

import static com.yoursway.hello.launching.internal.HelloLaunchConfigurationConstants.findInterpreterFrame;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.dltk.internal.debug.core.model.ScriptStackFrame;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;

public class ScriptProxyThread implements IThread {

	final IThread thread;
	private final HelloDebugTarget debugTarget;
	private final JDIDebugTarget javaTarget;
	private static IStackFrame[] fakeScriptFrames;

	public boolean canResume() {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			return javaThread.canResume();
		else
			return thread.canResume();
	}

	public boolean canStepInto() {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			return javaThread.canStepInto();
		else
			return thread.canStepInto();
	}

	public boolean canStepOver() {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			return javaThread.canStepOver();
		else
			return thread.canStepOver();
	}

	public boolean canStepReturn() {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			return javaThread.canStepReturn();
		else
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
		List<IStackFrame> frames = new ArrayList<IStackFrame>();
		addJavaFrames(frames);
		addScriptFrames(frames);
		return frames.toArray(new IStackFrame[frames.size()]);
	}

	private void addScriptFrames(List<IStackFrame> frames)
			throws DebugException {
		IThread javaThread = getJavaThread();
		if (!javaThread.isSuspended())
			fakeScriptFrames = thread.getStackFrames();
		IStackFrame[] originals = fakeScriptFrames;
		for (IStackFrame original : originals) {
			frames.add(new ScriptProxyFrame(this, (ScriptStackFrame) original));
		}
	}

	private void addJavaFrames(List<IStackFrame> frames)
			throws DebugException {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.hasStackFrames()) {
			IStackFrame[] frr = javaThread.getStackFrames();
			int fr = findInterpreterFrame(frr);
			if (fr >= 0)
				for (int i = 0; i < fr; i++)
					frames.add(frr[i]);
		}
	}

	private IThread getJavaThread() {
		IThread[] javaThreads = javaTarget.getThreads();
		for (IThread thread : javaThreads)
			try {
				if (!((JDIThread) thread).isDaemon())
					return thread;
			} catch (DebugException e) {
				e.printStackTrace();
			}
		return null;
	}
	
	private boolean javaThreadHasFrames() throws DebugException {
		IThread javaThread = getJavaThread();
		return javaThread != null && javaThread.hasStackFrames();
	}
	
	private boolean javaThreadIsSuspended() {
		IThread javaThread = getJavaThread();
		return javaThread != null && javaThread.isSuspended();
	}

	public IStackFrame getTopStackFrame() throws DebugException {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			return javaThread.getTopStackFrame();
			
		IStackFrame top = thread.getTopStackFrame();
		if (top == null)
			return null;
		return new ScriptProxyFrame(this, (ScriptStackFrame) top);
	}

	public boolean hasStackFrames() throws DebugException {
		return thread.hasStackFrames() || javaThreadHasFrames();
	}

	public boolean isStepping() {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			return javaThread.isStepping();
		else
			return thread.isStepping();
	}

	public boolean isSuspended() {
		return thread.isSuspended() || javaThreadIsSuspended();
	}

	public boolean isTerminated() {
		return thread.isTerminated();
	}

	public void resume() throws DebugException {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			javaThread.resume();
		else
			thread.resume();
	}

	public void stepInto() throws DebugException {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			javaThread.stepInto();
		else
			thread.stepInto();
	}

	public void stepOver() throws DebugException {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			javaThread.stepOver();
		else
			thread.stepOver();
	}

	public void stepReturn() throws DebugException {
		IThread javaThread = getJavaThread();
		if (javaThread != null && javaThread.isSuspended())
			javaThread.stepReturn();
		else
			thread.stepReturn();
	}

	public void suspend() throws DebugException {
		thread.suspend();
	}

	public void terminate() throws DebugException {
		thread.terminate();
		IThread javaThread = getJavaThread();
		if (javaThread != null)
			javaThread.terminate();
	}

	@SuppressWarnings("restriction")
	public ScriptProxyThread(IThread thread, HelloDebugTarget debugTarget,
			JDIDebugTarget javaTarget) {
		this.thread = thread;
		this.debugTarget = debugTarget;
		this.javaTarget = javaTarget;
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
