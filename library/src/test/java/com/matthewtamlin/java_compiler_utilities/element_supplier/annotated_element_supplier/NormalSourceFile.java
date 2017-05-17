package com.matthewtamlin.java_compiler_utilities.element_supplier.annotated_element_supplier;

public class NormalSourceFile {
	@UsedOnceAnnotation
	public boolean field1;
	
	@UsedTwiceAnnotation
	private String method1() {
		return "something";
	}
	
	@UsedTwiceAnnotation
	public char method2() {
		return '1';
	}
}