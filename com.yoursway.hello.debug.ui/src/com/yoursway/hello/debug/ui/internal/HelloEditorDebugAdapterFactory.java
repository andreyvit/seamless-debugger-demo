package com.yoursway.hello.debug.ui.internal;

import org.eclipse.debug.ui.actions.IRunToLineTarget;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.dltk.debug.ui.ScriptEditorDebugAdapterFactory;
import org.eclipse.dltk.debug.ui.breakpoints.ScriptToggleBreakpointAdapter;
import org.eclipse.dltk.internal.debug.ui.ScriptRunToLineAdapter;

import com.yoursway.hello.debug.ui.internal.model.HelloLineBreakpointAdapter;

/**
 * Debug adapter factory for the Ruby editor.
 */
public class HelloEditorDebugAdapterFactory extends
		ScriptEditorDebugAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IRunToLineTarget.class) {
			return new ScriptRunToLineAdapter();
		} else if (adapterType == IToggleBreakpointsTarget.class) {
			return new HelloLineBreakpointAdapter();
		}

		return null;
	}

	@Override
	protected ScriptToggleBreakpointAdapter getBreakpointAdapter() {
		return null;
	}

}
