package com.yoursway.hello.ui;

import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class HelloBuildpathsBlock extends BuildpathsBlock {
    public HelloBuildpathsBlock(IRunnableContext runnableContext, IStatusChangeListener context,
            int pageToShow, boolean useNewPage, IWorkbenchPreferenceContainer pageContainer) {
        super(runnableContext, context, pageToShow, useNewPage, pageContainer);
    }
    
    @Override
    protected IPreferenceStore getPreferenceStore() {
        return HelloUIPlugin.getDefault().getPreferenceStore();
    }
    
    @Override
    protected boolean supportZips() {
        return false;
    }
}
