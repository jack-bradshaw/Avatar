package com.matthewtamlin.java_compiler_utilities.element_util.collectors;

import com.google.common.collect.ImmutableSet;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A collector which only collects elements annotated with a specific annotation.
 */
public class AnnotatedElementCollector extends ElementCollector<Set<Element>> {
	/**
	 * The elements which have been collected during processing.
	 */
	private final Set<Element> collectedElements = new HashSet<>();
	
	/**
	 * The annotation to search for. Elements will only be collected if they are annotated with this annotation.
	 */
	private final Class<? extends Annotation> targetAnnotation;
	
	/**
	 * Constructs a new AnnotatedElementCollector. The collector will collect all elements found during processing
	 * which are annotated with the supplied annotation.
	 *
	 * @param targetAnnotation
	 * 		the annotation to search for when collecting elements, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code targetAnnotation} is null
	 */
	public AnnotatedElementCollector(final Class<? extends Annotation> targetAnnotation) {
		checkNotNull(targetAnnotation, "Argument \'targetAnnotation\' cannot be null.");
		
		this.targetAnnotation = targetAnnotation;
	}
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return ImmutableSet.of(targetAnnotation.getCanonicalName());
	}
	
	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		collectedElements.addAll(roundEnv.getElementsAnnotatedWith(targetAnnotation));
		
		return false;
	}
	
	@Override
	public Set<Element> getCollectedElements() {
		return new HashSet<>(collectedElements);
	}
}