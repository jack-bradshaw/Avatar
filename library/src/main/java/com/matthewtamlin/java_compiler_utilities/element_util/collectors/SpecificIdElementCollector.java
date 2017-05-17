package com.matthewtamlin.java_compiler_utilities.element_util.collectors;

import com.google.common.collect.ImmutableSet;
import com.matthewtamlin.java_compiler_utilities.element_util.element_supplier.ElementId;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A collector which collects elements based on ID, where element IDs are defined using the {@link ElementId}
 * annotation.
 */
public class SpecificIdElementCollector extends ElementCollector<Set<Element>> {
	/**
	 * The elements which have been collected during processing.
	 */
	private final Set<Element> collectedElements = new HashSet<>();
	
	/**
	 * The ID to search for. Elements will only be collected if they have an ElementId annotation with this ID.
	 */
	private final String id;
	
	/**
	 * Constructs a new SpecificIdElementCollector. The collector will collect all elements found during processing
	 * which have the supplied ID (as defined by {@link ElementId} annotations in the source).
	 *
	 * @param id
	 * 		the ID to search for when collecting elements, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code id} is null
	 */
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