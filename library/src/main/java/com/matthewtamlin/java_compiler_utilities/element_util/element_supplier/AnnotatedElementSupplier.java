package com.matthewtamlin.java_compiler_utilities.element_util.element_supplier;

import com.matthewtamlin.java_compiler_utilities.element_util.collectors.AnnotatedElementCollector;
import com.matthewtamlin.java_compiler_utilities.element_util.collectors.ElementCollector;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.lang.annotation.Annotation;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class AnnotatedElementSupplier {
	
	private final JavaFileObject source;
	
	public AnnotatedElementSupplier(final JavaFileObject source) {
		this.source = checkNotNull(source, "Argument \'source\' cannot be null.");
	}
	
	public Set<Element> getElements(final Class<? extends Annotation> targetAnnotation)
			throws CompilerMissingException {
		
		checkNotNull(targetAnnotation, "Argument \'targetAnnotation\' cannot be null.");
		
		final ElementCollector<Set<Element>> collector = new AnnotatedElementCollector(targetAnnotation);
		
		CompilerUtil.compileUsingCollector(source, collector);
		
		return collector.getCollectedElements();
	}
}