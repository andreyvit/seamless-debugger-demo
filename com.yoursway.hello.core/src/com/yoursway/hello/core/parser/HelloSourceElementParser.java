package com.yoursway.hello.core.parser;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.ISourceElementParser;
import org.eclipse.dltk.core.ISourceModuleInfoCache.ISourceModuleInfo;

import com.yoursway.hello.core.HelloNature;

public class HelloSourceElementParser implements ISourceElementParser {
    
    private ISourceElementRequestor requestor;
    private IProblemReporter reporter;
    
    public HelloSourceElementParser() {
    }
    
    public HelloSourceElementParser(ISourceElementRequestor requestor, IProblemReporter reporter) {
        this.requestor = requestor;
        this.reporter = reporter;
    }
    
    public void parseSourceModule(char[] contents, ISourceModuleInfo astCashe, char[] filename) {
        String content = new String(contents);
        
        ISourceParser sourceParser;
        sourceParser = DLTKLanguageManager.getSourceParser(HelloNature.NATURE_ID);
        
        ModuleDeclaration moduleDeclaration = sourceParser.parse(null, content.toCharArray(), reporter);
        
        if (moduleDeclaration != null) {
            HelloSourceElementRequestor sourceElementRequestor = new HelloSourceElementRequestor(requestor);
            
            try {
                moduleDeclaration.traverse(sourceElementRequestor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setReporter(IProblemReporter reporter) {
        this.reporter = reporter;
    }
    
    public void setRequestor(ISourceElementRequestor requestor) {
        this.requestor = requestor;
    }
    
}
