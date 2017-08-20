package com.matthewtamlin.avatar.rules.avatar_rule.with_running;

import com.matthewtamlin.avatar.rules.ElementId;

@ElementId("public root")
public class Data {
	public void methodA() {}
	
	@Annotation1
	@ElementId("ID1")
	protected void methodB() {}
	
	@Annotation2
	@ElementId("ID2")
	Object fieldA;
	
	@Annotation2
	@ElementId("ID2")
	private String fieldB;
	
	@ElementId("method")
	private void methodC() {}
	
	@ElementId("field")
	public Object fieldC;
	
	@ElementId("class")
	public static class ClassA {}
	
	@ElementId("interface")
	public interface InterfaceA {}
	
	public void methodD(@ElementId("parameter") String parameterA) {}
}