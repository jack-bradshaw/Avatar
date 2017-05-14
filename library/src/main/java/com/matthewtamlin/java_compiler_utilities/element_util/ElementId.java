package com.matthewtamlin.java_compiler_utilities.element_util;

/**
 * Identifies an element so that the ElementUtil can retrieve it.
 */
public @interface ElementId {
	/**
	 * @return an identifier which is unique within a single Java file
	 */
	int value();
}