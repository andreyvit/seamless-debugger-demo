package com.yoursway.hello.core.util;

public class Strings {
    
    public static String join(String[] array, String delim) {
        StringBuilder res = new StringBuilder(array.length * (100 + delim.length()));
        for (String s : array)
            (res.length() > 0 ? res.append(delim) : res).append(s);
        return res.toString();
    }
    
}
