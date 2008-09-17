package com.yoursway.hello.debug;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.InterpreterStandin;
import org.eclipse.dltk.launching.ScriptRuntime;

import com.yoursway.hello.core.HelloNature;

public class HelloLaunchingUtils {
    
    public static List<InterpreterStandin> interpreters() {
        List<InterpreterStandin> standins = new ArrayList<InterpreterStandin>();
        IInterpreterInstallType[] types = ScriptRuntime.getInterpreterInstallTypes(HelloNature.NATURE_ID);
        for (IInterpreterInstallType type : types) {
            IInterpreterInstall[] installs = type.getInterpreterInstalls();
            for (int j = 0; j < installs.length; j++) {
                IInterpreterInstall install = installs[j];
                standins.add(new InterpreterStandin(install));
            }
        }
        return standins;
    }
    
}
