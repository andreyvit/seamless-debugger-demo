package com.yoursway.hello.core.parser;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ast.parser.ISourceParserFactory;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

import com.yoursway.hello.core.HelloNature;

public class HelloSourceParser extends AbstractSourceParser implements ISourceParser, ISourceParserFactory {
    
    public ModuleDeclaration parse(String fileName, String source, IProblemReporter reporter) {
        return parse(fileName.toCharArray(), source.toCharArray(), reporter);
    }
    
    public ModuleDeclaration parse(char[] fileName, char[] source, IProblemReporter reporter) {
        ModuleDeclaration program = new ModuleDeclaration(0);
        return program;
    }
    
    @Override
    public String getDescription() {
        return null;
    }
    
    @Override
    public String getId() {
        return null;
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public String getNatureId() {
        return HelloNature.NATURE_ID;
    }
    
    @Override
    public String getPreferencePageId() {
        return null;
    }
    
    @Override
    public int getPriority() {
        return 0;
    }
    
    @Override
    public String getPropertyPageId() {
        return null;
    }
    
    public ISourceParser createSourceParser() {
        return new HelloSourceParser();
    }
    
}
