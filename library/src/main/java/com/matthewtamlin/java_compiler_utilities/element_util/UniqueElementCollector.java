package com.matthewtamlin.java_compiler_utilities.element_util;

import com.google.common.collect.ImmutableSet;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class UniqueElementCollector extends ElementCollector<Element> {
	private final int id;
	
	private Element collectedElement;
	
	public UniqueElementCollector(final int id) {
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
				collectedElement = element;
				break;
			}
		}
		
		return false;
	}
	
	@Override
	public Element getResult() {
		return collectedElement;
	}
}
