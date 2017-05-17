package com.matthewtamlin.java_compiler_utilities.element_util.element_supplier;

import com.matthewtamlin.java_compiler_utilities.element_util.collectors.ElementCollector;
import com.matthewtamlin.java_compiler_utilities.element_util.collectors.RootElementCollector;

import javax.lang.model.element.Element;
import java.util.Set;

/**
 * Gets all root elements from a JavaFileObject.
 */
public class RootElementSupplier extends CollectorBasedElementSupplier<Set<Element>> {
	@Override
	protected ElementCollector<Set<Element>> getNewCollector() {
		return new RootElementCollector();
	}
}