package com.matthewtamlin.avatar.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class TestIterableNullChecker {
	@Test(expected = IllegalArgumentException.class)
	public void testCheckNotContainsNull_nullIterable() {
		IterableNullChecker.checkNotContainsNull(null, "");
	}
	
	@Test
	public void testCheckNotContainsNull_emptyIterable() {
		IterableNullChecker.checkNotContainsNull(new ArrayList<>(), "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCheckNotContainsNull_iterableContainingOnlyNull() {
		final List<Object> list = new ArrayList<>();
		list.add(null);
		
		IterableNullChecker.checkNotContainsNull(list, "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCheckNotContainsNull_iterableContainingNullAndOtherValues() {
		final List<Object> list = new ArrayList<>();
		list.add(null);
		list.add("hello");
		
		IterableNullChecker.checkNotContainsNull(list, "");
	}
	
	@Test
	public void testCheckNotContainsNull_iterableContainingOnlyNonNullValues() {
		final List<Object> list = new ArrayList<>();
		list.add("hello");
		list.add("world");
		
		IterableNullChecker.checkNotContainsNull(list, "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCheckNotContainsNull_nullErrorMessage() {
		IterableNullChecker.checkNotContainsNull(new ArrayList<>(), null);
	}
}