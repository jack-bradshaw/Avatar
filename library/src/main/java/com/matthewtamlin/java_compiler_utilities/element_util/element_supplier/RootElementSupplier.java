package com.matthewtamlin.java_compiler_utilities.element_util.element_supplier;

import com.matthewtamlin.java_compiler_utilities.element_util.collectors.ElementCollector;
import com.matthewtamlin.java_compiler_utilities.element_util.collectors.RootElementCollector;
import com.matthewtamlin.java_compiler_utilities.element_util.collectors.SpecificIdElementCollector;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Gets all root elements from a JavaFileObject.
 */
public class RootElementSupplier {
	private JavaFileObject source;
	
	public RootElementSupplier(final JavaFileObject source) {
		this.source = checkNotNull(source, "Argument \'source\' cannot be null.");
	}
	
	public Set<Element> getElements(final String id) throws CompilerMissingException {
		checkNotNull(id, "Argument \'id\' cannot be null.");
		
		final ElementCollector<Set<Element>> collector = new RootElementCollector();
		
		CompilerUtil.compileUsingCollector(source, collector);
		
		return collector.getCollectedElements();
	}
	
	
}