package com.matthewtamlin.avatar.rules.avatar_rule.after_running_rule;

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
}