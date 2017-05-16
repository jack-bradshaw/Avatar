package com.matthewtamlin.java_compiler_utilities.element_util.collectors;

import com.google.common.collect.ImmutableSet;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkEachElementIsNotNull;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A collector which only collects elements tagged with specific annotations.
 */
public class TaggedElementCollector extends ElementCollector<ImmutableSet<Element>> {
	private final Set<Element> collectedElements = new HashSet<>();
	
	/**
	 * The tags to search for. Elements will only be collected if they have at least one of the contained annotations.
	 */
	private final Set<Class<? extends Annotation>> tags;
	
	/**
	 * Constructs a new TaggedElementCollector. The collector will collect all received annotations which have at
	 * least one of the supplied tags.
	 *
	 * @param tags
	 * 		the tags to use when collecting elements, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code tags} is null
	 */
	public TaggedElementCollector(final Set<Class<? extends Annotation>> tags) {
		checkNotNull(tags, "Argument \'tags\' cannot be null.");
		checkEachElementIsNotNull(tags, "Argument \'tags\' cannot contain null.");
		
		this.tags = ImmutableSet.copyOf(tags);
	}
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		final Set<String> targetClassesFullyQualified = new HashSet<>();
		
		for (final Class<? extends Annotation> targetClass : tags) {
			targetClassesFullyQualified.add(targetClass.getCanonicalName());
		}
		
		return targetClassesFullyQualified;
	}
	
	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		
		for (final Class<? extends Annotation> tagClass : tags) {
			collectedElements.addAll(roundEnv.getElementsAnnotatedWith(tagClass));
		}
		
		return false;
	}
	
	@Override
	public ImmutableSet<Element> getCollectedElements() {
		return ImmutableSet.copyOf(collectedElements);
	}
}