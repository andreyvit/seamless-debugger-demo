/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.launching.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventFilter;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.dbgp.DbgpSessionIdGenerator;
import org.eclipse.dltk.dbgp.IDbgpNotification;
import org.eclipse.dltk.dbgp.IDbgpNotificationListener;
import org.eclipse.dltk.dbgp.IDbgpNotificationManager;
import org.eclipse.dltk.dbgp.IDbgpSession;
import org.eclipse.dltk.dbgp.exceptions.DbgpException;
import org.eclipse.dltk.debug.core.DLTKDebugPlugin;
import org.eclipse.dltk.debug.core.IDbgpService;
import org.eclipse.dltk.debug.core.ScriptDebugManager;
import org.eclipse.dltk.debug.core.model.IScriptDebugTarget;
import org.eclipse.dltk.internal.debug.core.model.ScriptDebugTarget;
import org.eclipse.dltk.internal.debug.core.model.ScriptStackFrame;
import org.eclipse.dltk.internal.debug.core.model.ScriptThread;
import org.eclipse.dltk.internal.launching.InterpreterMessages;
import org.eclipse.dltk.launching.AbstractScriptLaunchConfigurationDelegate;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.jdt.debug.core.IJavaMethodBreakpoint;
import org.eclipse.jdt.debug.core.JDIDebugModel;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;
import org.eclipse.jdt.internal.launching.JavaSourceLookupDirector;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.w3c.dom.Element;

import com.yoursway.hello.core.HelloNature;

@SuppressWarnings("restriction")
public class HelloLaunchConfigurationDelegate extends
		AbstractScriptLaunchConfigurationDelegate {

	private JavaSourceLookupDirector javaSourceLookupDirector;
	private String sessionId;

	public String getLanguageId() {
		return HelloNature.NATURE_ID;
	}

	protected void validateLaunchConfiguration(
			ILaunchConfiguration configuration, String mode, IProject project)
			throws CoreException {
	}

	protected void launchJava(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager
				.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		ILaunchConfigurationWorkingCopy wc = type.newInstance(null,
				"SampleConfig");
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
				"hello");
		wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
				"com.yoursway.hello.interpreter.Hello");
		wc.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
				getScriptLaunchPath(configuration)
						+ " "
						+ mode
						+ " "
						+ DLTKDebugPlugin.getDefault().getDbgpService()
								.getPort() + " " + sessionId);
		ILaunchConfiguration config = wc.doSave();

		javaSourceLookupDirector = new JavaSourceLookupDirector();
		javaSourceLookupDirector.initializeDefaults(config);

		JavaLaunchDelegate launchDelegate = new JavaLaunchDelegate();
		launchDelegate.launch(config, mode, launch, monitor);
	}

	protected PreferencesLookupDelegate createPreferencesLookupDelegate(
			ILaunch launch) throws CoreException {
		IScriptProject sProject = ScriptRuntime.getScriptProject(launch
				.getLaunchConfiguration());
		return new PreferencesLookupDelegate(sProject.getProject());
	}

	public String getDebugModelId() {
		return ScriptDebugManager.getInstance().getDebugModelByNature(
				HelloNature.NATURE_ID);
	}

	protected String getSessionId(ILaunchConfiguration configuration)
			throws CoreException {
		return DbgpSessionIdGenerator.generate();
	}

	protected IScriptDebugTarget addDebugTarget(ILaunch launch,
			IDbgpService dbgpService, final JDIDebugTarget javaTarget)
			throws CoreException {

		final HelloDebugTarget target = new HelloDebugTarget(getDebugModelId(),
				dbgpService, sessionId, launch, null, javaTarget);

		launch.addDebugTarget(target);

		DebugPlugin.getDefault().addDebugEventFilter(new IDebugEventFilter() {

			public DebugEvent[] filterDebugEvents(DebugEvent[] events) {
				List<DebugEvent> newEvents = new ArrayList<DebugEvent>();
				for (DebugEvent event : events) {
					newEvents.add(event);
					DebugEvent newEvent = wrap(event);
					if (newEvent != null)
						newEvents.add(newEvent);
				}
				return newEvents.toArray(new DebugEvent[newEvents.size()]);
			}

			private DebugEvent wrap(DebugEvent event) {
				System.out.println("EVENT: " + event);
				Object source = event.getSource();
				Object newSource = wrap(target, source);
				if (newSource == null)
					return null;
				Object data = event.getData();
				event = new DebugEvent(newSource, event.getKind(), event
						.getDetail());
				if (data != null)
					System.out.println();
				event.setData(data);
				return event;
			}

			private Object wrap(final HelloDebugTarget target, Object source) {
				Object newSource = null;
				if (source instanceof ScriptProxyFrame)
					return null;
				if (source instanceof ScriptProxyThread)
					return null;
				if (source instanceof ScriptStackFrame)
					newSource = wrapFrame(target, (ScriptStackFrame) source);
				else if (source instanceof ScriptThread)
					newSource = wrapThread(target, (IThread) source);
				else if (source instanceof JDIDebugTarget)
					newSource = target;
				else if (source instanceof JDIThread)
					try {
						newSource = target.getThreads()[0];
					} catch (DebugException e) {
						e.printStackTrace();
					}
				return newSource;
			}

			private ScriptProxyFrame wrapFrame(final HelloDebugTarget target,
					ScriptStackFrame frame) {
				return new ScriptProxyFrame(wrapThread(target, frame
						.getThread()), frame);
			}

			private ScriptProxyThread wrapThread(final HelloDebugTarget target,
					IThread thread) {
				return new ScriptProxyThread(thread, target, javaTarget);
			}

		});

		return target;
	}

	public static final String LAUNCH_ATTR_DEBUGGING_ENGINE_ID = "debugging_engine_id"; //$NON-NLS-1$

	public static final String ENGINE_ID = "com.yoursway.hello.debug.engine"; //$NON-NLS-1$
	private IJavaMethodBreakpoint extLibEntryBreakpoint;

	protected String getDebuggingEngineId() {
		return ENGINE_ID;
	}
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			final ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		sessionId = getSessionId(configuration);
		launchJava(configuration, mode, launch, monitor);
		final ISourceLocator oldSourceLocator = launch.getSourceLocator();
		launch.setSourceLocator(new ISourceLocator() {

			public Object getSourceElement(IStackFrame stackFrame) {
				Object element = oldSourceLocator.getSourceElement(stackFrame);
				if (element != null)
					return element;
				return javaSourceLookupDirector.getSourceElement(stackFrame);
			}

		});

		JDIDebugTarget debugTarget = (JDIDebugTarget) launch.getDebugTarget();
		DebugPlugin.getDefault().addDebugEventListener(
				new IDebugEventSetListener() {

					public void handleDebugEvents(DebugEvent[] events) {
						for (DebugEvent ev : events) {
							if (ev.getKind() == DebugEvent.SUSPEND) {
								if (ev.getSource() instanceof JDIThread) {
									JDIThread thread = (JDIThread) ev
											.getSource();
									try {
										IStackFrame[] stackFrames = thread
												.getStackFrames();
										if (HelloLaunchConfigurationConstants.findInterpreterFrame(stackFrames) == 0) {
											thread.resume();
										}
									} catch (DebugException e) {
										e.printStackTrace();
									}

								}
							}
						}
					}

				});
		// debugTarget.addJDIEventListener(new IJDIEventListener() {}, request)
		// mainJavaThread.resume();

		// interpreter is already launched, so we just need to setup the dbgp
		// session
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			monitor.beginTask(
					InterpreterMessages.DebuggingEngineRunner_launching, 5);
			if (monitor.isCanceled()) {
				return;
			}
			try {

				final IDbgpService service = DLTKDebugPlugin.getDefault()
						.getDbgpService();

				if (!service.available()) {
					abort(InterpreterMessages.errDbgpServiceNotAvailable, null);
				}

				final IScriptDebugTarget target = addDebugTarget(launch,
						service, debugTarget);

				// Disable the output of the debugging engine process
				launch.setAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT,
						Boolean.FALSE.toString());

				// Debugging engine id
				launch.setAttribute(LAUNCH_ATTR_DEBUGGING_ENGINE_ID,
						getDebuggingEngineId());

				// // Configuration
				// final DbgpInterpreterConfig dbgpConfig = new
				// DbgpInterpreterConfig(
				// config);
				//
				// InterpreterConfig newConfig = addEngineConfig(config,
				// prefDelegate);

				// Starting debugging engine
				IProcess process = launch.getProcesses()[0];
				// try {
				// DebugEventHelper.fireExtendedEvent(null,
				// ExtendedDebugEventDetails.BEFORE_VM_STARTED);
				//
				// // Running
				// monitor
				// .subTask(InterpreterMessages.DebuggingEngineRunner_running);
				// // process = rawRun(launch, newConfig);
				// } catch (CoreException e) {
				// abort(InterpreterMessages.errDebuggingEngineNotStarted, e);
				// }
				// monitor.worked(4);

				// Waiting for debugging engine connect
				waitDebuggerConnected(process, launch, monitor);

				ScriptThread thread = (ScriptThread) ((HelloDebugTarget) target)
						.getOriginalThreads()[0];
				final IDbgpSession session = thread.getDbgpSession();
				IDbgpNotificationManager nm = session.getNotificationManager();
				nm.addNotificationListener(new IDbgpNotificationListener() {

					public void dbgpNotify(IDbgpNotification notification) {
						if ("lib_call_coming".equalsIgnoreCase(notification
								.getName())) {
							Element body = notification.getBody();
							String className = body.getAttribute("class");
							String methodName = body.getAttribute("method");

							try {
								extLibEntryBreakpoint = JDIDebugModel
										.createMethodBreakpoint(ResourcesPlugin
												.getWorkspace().getRoot(),
												className,
												methodName, //$NON-NLS-1$
												"()V", //$NON-NLS-1$
												true, false, false, -1, -1, -1,
												1, false, new HashMap());
								extLibEntryBreakpoint.setPersisted(false);
								extLibEntryBreakpoint.setEnabled(true);

								launch.getDebugTarget().breakpointAdded(
										extLibEntryBreakpoint);

								session.getExtendedCommands().execute(
										"doitbaby");
							} catch (CoreException e) {
								e.printStackTrace();
							} catch (DbgpException e) {
								e.printStackTrace();
							}
						}
					}

				});
			} catch (CoreException e) {
				launch.terminate();
				throw e;
			} finally {
				monitor.done();
			}
			// Happy debugging :)
		}
	}

	/**
	 * Waiting debugging process to connect to current launch
	 * 
	 * @param debuggingProcess
	 *            process that will connect to current launch or null if handle
	 *            to process is not available (remote debugging)
	 * @param launch
	 *            launch to connect to
	 * @param monitor
	 *            progress monitor
	 * @throws CoreException
	 *             if debuggingProcess terminated, monitor is canceled or // *
	 *             timeout
	 */
	protected void waitDebuggerConnected(IProcess debuggingProcess,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		final int WAIT_CHUNK = 100;

		int timeout = 150000;

		ScriptDebugTarget target = null;
		IDebugTarget[] debugTargets = launch.getDebugTargets();
		for (IDebugTarget t : debugTargets) {
			if (t instanceof ScriptDebugTarget) {
				target = (ScriptDebugTarget) t;
				break;
			}
		}
		target.setProcess(debuggingProcess);

		try {
			int all = 0;
			while (timeout == 0 || all < timeout) {
				if (target.isInitialized()
						|| target.isTerminated()
						|| monitor.isCanceled()
						|| (debuggingProcess != null && debuggingProcess
								.isTerminated()))
					break;

				Thread.sleep(WAIT_CHUNK);
				all += WAIT_CHUNK;
			}
		} catch (InterruptedException e) {
			Thread.interrupted();
		}

		if (!target.isInitialized()) {
			if (debuggingProcess != null && debuggingProcess.canTerminate()) {
				debuggingProcess.terminate();
			}
			abort(InterpreterMessages.errDebuggingEngineNotConnected, null);
		}
	}

}
