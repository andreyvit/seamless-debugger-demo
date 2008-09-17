/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package com.yoursway.hello.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class HelloPartitionScanner extends RuleBasedPartitionScanner {
    
    /**
     * Creates the partitioner and sets up the appropriate rules.
     */
    public HelloPartitionScanner() {
        super();
        List<IPredicateRule> rules = new ArrayList<IPredicateRule>();
        IPredicateRule[] result = new IPredicateRule[rules.size()];
        rules.toArray(result);
        setPredicateRules(result);
    }
    
    public int getOffsetForLaterContextLookup() {
        return fOffset;
    }
    
    protected Token getToken(String key) {
        return null;
    }
}