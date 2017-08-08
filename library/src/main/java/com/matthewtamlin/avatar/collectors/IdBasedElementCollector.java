package com.matthewtamlin.avatar.collectors;

import com.google.common.collect.ImmutableSet;
import com.matthewtamlin.avatar.element_supplier.ElementId;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Collects elements based on their IDs, where IDs are defined by applying the {@link ElementId} annotation to the
 * source code.
 */
public class IdBasedElementCollector extends ElementCollector<Set<Element>> {
	/**
	 * The elements which have been collected during processing.
	 */
	private final Set<Element> collectedElements = new HashSet<>();
	
	/**
	 * Elements will only be collected if they have an ElementId annotation with this ID.
	 */
	private final String id;
	
	/**
	 * Constructs a new IdBasedElementCollector. Only elements with the supplied ID will be collected, where IDs are
	 * defined by applying the {@link ElementId} to the source.
	 *
	 * @param id
	 * 		the ID to search for when collecting elements, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code id} is null
	 */
	public IdBasedElementCollector(final String id) {
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
	public Set<Element> getCollectedElements() {
		return new HashSet<>(collectedElements);
	}
}