package com.matthewtamlin.java_compiler_utilities.element_util;

import com.google.common.collect.ImmutableSet;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

public class SpecificIdElementCollector extends ElementCollector<ImmutableSet<Element>> {
	private final Set<Element> collectedElements = new HashSet<>();
	
	private final int id;
	
	public SpecificIdElementCollector(final int id) {
		this.id = id;
	}
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return ImmutableSet.of("*");
	}
	
	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		for (final Element element : roundEnv.getElementsAnnotatedWith(ElementId.class)) {
			final ElementId idProvider = element.getAnnotation(ElementId.class);
			
			if (idProvider.value() == id) {
				collectedElements.add(element);
			}
		}
		
		return false;
	}
	
	@Override
	public ImmutableSet<Element> getCollectedElements() {
		return ImmutableSet.copyOf(collectedElements);
	}
}