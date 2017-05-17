package com.matthewtamlin.java_compiler_utilities.element_util.element_supplier;

import com.matthewtamlin.java_compiler_utilities.element_util.collectors.RootElementCollector;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Gets all root elements from a JavaFileObject.
 */
public class RootElementSupplier implements ElementSupplier<Set<Element>> {
	@Override
	public Set<Element> getFrom(final JavaFileObject source) throws CompilerMissingException {
		checkNotNull(source, "Argument \'source\' cannot be null.");
		
		final RootElementCollector collector = new RootElementCollector();
		
		CompilerUtil.compileUsingCollector(source, collector);
		
		return collector.getCollectedElements();
	}
}