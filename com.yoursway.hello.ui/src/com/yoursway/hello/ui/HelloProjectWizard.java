package com.yoursway.hello.ui;

import java.util.Observable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.dltk.ui.wizards.NewElementWizard;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.ProjectWizardSecondPage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import com.yoursway.hello.core.HelloNature;

public class HelloProjectWizard extends NewElementWizard {
    
    public static final String WIZARD_ID = "com.yoursway.hello.ui.helloProjectWizard";
    
    private ProjectWizardFirstPage fFirstPage;
    private ProjectWizardSecondPage fSecondPage;
    private IConfigurationElement fConfigElement;
    
    public HelloProjectWizard() {
        setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
        setWindowTitle("New Hello Project");
    }
    
    @Override
    public void addPages() {
        super.addPages();
        fFirstPage = new ProjectWizardFirstPage() {
            
            HelloInterpreterGroup fInterpreterGroup;
            
            final class HelloInterpreterGroup extends AbstractInterpreterGroup {
                public HelloInterpreterGroup(Composite composite) {
                    super(composite);
                }
                
                @Override
                protected String getCurrentLanguageNature() {
                    return HelloNature.NATURE_ID;
                }
                
                @Override
                protected String getIntereprtersPreferencePageId() {
                    return "com.yoursway.hello.preferences.interpreters";
                }
            };
            
            @Override
            protected void createInterpreterGroup(Composite parent) {
                fInterpreterGroup = new HelloInterpreterGroup(parent);
            }
            
            @Override
            protected Observable getInterpreterGroupObservable() {
                return fInterpreterGroup;
            }
            
            @Override
            protected boolean supportInterpreter() {
                return true;
            }
            
            @Override
            protected IInterpreterInstall getInterpreter() {
                return fInterpreterGroup.getSelectedInterpreter();
            }
            
            @Override
            protected void handlePossibleInterpreterChange() {
                fInterpreterGroup.handlePossibleInterpreterChange();
            }
            
            @Override
            protected boolean interpeterRequired() {
                return false;
            }
        };
        
        // First page
        fFirstPage.setTitle("First page title");
        fFirstPage.setDescription("First page desc");
        addPage(fFirstPage);
        
        // Second page
        fSecondPage = new ProjectWizardSecondPage(fFirstPage) {
            @Override
            protected BuildpathsBlock createBuildpathBlock(IStatusChangeListener listener) {
                return new HelloBuildpathsBlock(new BusyIndicatorRunnableContext(), listener, 0,
                        useNewSourcePage(), null);
            }
            
            @Override
            protected String getScriptNature() {
                return HelloNature.NATURE_ID;
            }
            
            @Override
            protected IPreferenceStore getPreferenceStore() {
                return HelloUIPlugin.getDefault().getPreferenceStore();
            }
        };
        addPage(fSecondPage);
    }
    
    @Override
    protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
        fSecondPage.performFinish(monitor); // use the full progress monitor
    }
    
    @Override
    public boolean performFinish() {
        boolean res = super.performFinish();
        if (res) {
            BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
            selectAndReveal(fSecondPage.getScriptProject().getProject());
        }
        return res;
    }
    
    /*
     * Stores the configuration element for the wizard. The config element will
     * be used in <code>performFinish</code> to set the result perspective.
     */
    public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
        fConfigElement = cfig;
    }
    
    @Override
    public boolean performCancel() {
        fSecondPage.performCancel();
        return super.performCancel();
    }
    
    @Override
    public IModelElement getCreatedElement() {
        return DLTKCore.create(fFirstPage.getProjectHandle());
    }
    
}
