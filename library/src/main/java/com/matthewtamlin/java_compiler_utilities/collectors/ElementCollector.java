package com.matthewtamlin.java_compiler_utilities.collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.element.Element;
import java.util.Collection;

/**
 * An annotation processor which collects elements as it processes them. The implementation is free to discriminate
 * between elements and can choose which (if any) are collected.
 */
public abstract class ElementCollector<T extends Collection<? extends Element>> extends AbstractProcessor {
	/**
	 * Gets the elements which have been collected. This method will not return meaningful data unless processing is
	 * complete. This method might return an empty collection but it will never return null.
	 *
	 * @return the elements collected by this collector, not null
	 */
	public abstract T getCollectedElements();
}