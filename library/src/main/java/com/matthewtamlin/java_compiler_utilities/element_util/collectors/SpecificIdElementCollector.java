package com.matthewtamlin.java_compiler_utilities.element_util.collectors;

import com.google.common.collect.ImmutableSet;
import com.matthewtamlin.java_compiler_utilities.element_util.ElementId;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class SpecificIdElementCollector extends ElementCollector<ImmutableSet<Element>> {
	private final Set<Element> collectedElements = new HashSet<>();
	
	private final String id;
	
	public SpecificIdElementCollector(final String id) {
		this.id = checkNotNull(id, "Argument \'id\' cannot be null.");
	}
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return ImmutableSet.of("*");
	}
	
	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		for (final Element element : roundEnv.getElementsAnnotatedWith(ElementId.class)) {
			final ElementId annotation = element.getAnnotation(ElementId.class);
			
			if (id.equals(annotation.value())) {
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