package com.yoursway.hello;

public class Test {
	
	private static int counter = 0;
	
	public static void hello() {
		++counter;
		System.out.println("Hello " + counter + " times");
	}

}
