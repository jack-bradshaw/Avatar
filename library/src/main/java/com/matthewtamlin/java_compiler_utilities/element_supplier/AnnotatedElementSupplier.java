package com.matthewtamlin.java_compiler_utilities.element_supplier;

import com.matthewtamlin.java_compiler_utilities.collectors.AnnotatedElementCollector;
import com.matthewtamlin.java_compiler_utilities.collectors.ElementCollector;
import com.matthewtamlin.java_utilities.testing.Tested;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.lang.annotation.Annotation;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Gets specific elements from a {@link JavaFileObject} via annotations in the source code.
 */
@Tested(testMethod = "automated")
public class AnnotatedElementSupplier {
	/**
	 * The source to get elements from.
	 */
	private final JavaFileObject source;
	
	/**
	 * Constructs a new AnnotatedElementSupplier.
	 *
	 * @param source
	 * 		the JavaFileObject to get elements from, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code source} is null
	 */
	public AnnotatedElementSupplier(final JavaFileObject source) {
		this.source = checkNotNull(source, "Argument \'source\' cannot be null.");
	}
	
	/**
	 * Gets all elements from the source which have the supplied annotation. This method might return an empty set,
	 * but it will never return null.
	 *
	 * @param targetAnnotation
	 * 		the annotation to search for, not null
	 *
	 * @return all elements found in the source with the supplied annotation, not null
	 *
	 * @throws CompilerMissingException
	 * 		if there is no Java compiler available at runtime
	 * @throws IllegalArgumentException
	 * 		if {@code targetAnnotation} is null
	 */
	public Set<Element> getElementsWithAnnotation(final Class<? extends Annotation> targetAnnotation)
			throws CompilerMissingException {
		
		checkNotNull(targetAnnotation, "Argument \'targetAnnotation\' cannot be null.");
		
		final ElementCollector<Set<Element>> collector = new AnnotatedElementCollector(targetAnnotation);
		
		CompilerUtil.compileUsingCollector(source, collector);
		
		return collector.getCollectedElements();
	}
}