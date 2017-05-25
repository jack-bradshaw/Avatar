package com.matthewtamlin.avatar.element_supplier.id_based_element_supplier;

import com.matthewtamlin.avatar.element_supplier.ElementId;

public class NormalSourceFile {
	@ElementId("1")
	public boolean field1;
	
	@ElementId("2")
	private String method1() {
		return "something";
	}
	
	@ElementId("2")
	public char method2() {
		return '1';
	}
}