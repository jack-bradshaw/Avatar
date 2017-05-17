package com.matthewtamlin.java_compiler_utilities.element_util.collectors;

import com.google.common.collect.ImmutableSet;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Collects all received root elements.
 */
public class RootElementCollector extends ElementCollector<Set<Element>> {
	/**
	 * The elements which have been collected during processing.
	 */
	private final Set<Element> collectedElements = new HashSet<>();
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return ImmutableSet.of("*");
	}
	
	@Override
	public boolean process(final Set<? extends TypeElement> set, final RoundEnvironment roundEnvironment) {
		collectedElements.addAll(roundEnvironment.getRootElements());
		
		return false;
	}
	
	@Override
	public Set<Element> getCollectedElements() {
		return new HashSet<>(collectedElements);
	}
}