package com.matthewtamlin.avatar.element_supplier;

/**
 * Defines an ID for an element in a source file.
 */
public @interface ElementId {
	/**
	 * @return the ID for the annotated element, not necessary unique
	 */
	String value();
}