package com.yoursway.hello.core;

import org.eclipse.dltk.core.AbstractLanguageToolkit;

public class HelloLanguage extends AbstractLanguageToolkit {
    
    protected static HelloLanguage sToolkit = new HelloLanguage();
    
    public String getLanguageName() {
        return "Hello";
    }
    
    @Override
    public boolean languageSupportZIPBuildpath() {
        return false;
    }
    
    public String getNatureId() {
        return HelloNature.NATURE_ID;
    }
    
    public String getLanguageContentType() {
        return "com.yoursway.hello.core.content-type";
    }
    
    public static HelloLanguage getDefault() {
        return sToolkit;
    }
    
}
