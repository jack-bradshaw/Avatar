package com.matthewtamlin.java_compiler_utilities;

import com.matthewtamlin.java_compiler_utilities.element_supplier.ElementId;

public class NormalJavaFile {
	@Tag1
	private static final String constantWithTag1 = "Hello";
	
	@Tag2
	private static final String constantWithTag2 = "world!";
	
	private static final float constantWithoutTag = 100F;
	
	@Tag1
	private long fieldWithTag1 = 0L;
	
	@Tag2
	private int fieldWithTag2 = 100;
	
	private Object fieldWithoutTag = '\u0203';
	
	@ElementId("2")
	private boolean fieldWithId2 = true;
	
	@Tag1
	public void methodWithTag1(@Tag1 Object parameterWithTag1) {}
	
	@Tag2
	public boolean methodWithTag2(@Tag2 Object parameterWithTag2) {
		return true;
	}
	
	private short methodWithoutTag(Object parameterWithoutTag) {
		return 0;
	}
	
	@ElementId("1")
	private void methodWithId1() {}
	
	@ElementId("2")
	private String methodWithId2(String parameterWithoutTag) {
		return null;
	}
	
	@Tag1
	public class innerClassWithTag1 {}
	
	@Tag2
	public class innerClassWithTag2 {}
	
	public class innerClassWithoutTag {}
}

@Tag1
class DefaultClassWithTag1 {}

@Tag2
class DefaultClassWithTag2 {}

class DefaultClassWithoutTag {}