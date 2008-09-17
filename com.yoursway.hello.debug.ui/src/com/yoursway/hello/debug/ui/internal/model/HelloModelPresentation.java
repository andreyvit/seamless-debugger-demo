package com.yoursway.hello.debug.ui.internal.model;

import org.eclipse.dltk.debug.core.model.IScriptValue;
import org.eclipse.dltk.debug.ui.ScriptDebugModelPresentation;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.ui.IEditorInput;

import com.yoursway.hello.ui.HelloEditor;

public class HelloModelPresentation extends ScriptDebugModelPresentation {

	public String getEditorId(IEditorInput input, Object element) {
		String editorId = EditorUtility.getEditorID(input, element);
		if (editorId == null)
			editorId = HelloEditor.EDITOR_ID;

		return editorId;
	}

	public String getDetailPaneText(IScriptValue value) {
		return value.getRawValue();
	}
}
