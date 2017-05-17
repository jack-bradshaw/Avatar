package com.matthewtamlin.java_compiler_utilities.element_util.element_supplier;

import com.matthewtamlin.java_compiler_utilities.element_util.collectors.AnnotatedElementCollector;
import com.matthewtamlin.java_compiler_utilities.element_util.collectors.ElementCollector;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.lang.annotation.Annotation;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Gets all elements from a JavaFileObject which are marked with a particular annotation. The annotation to look
 * for is defined by passing its class to the constructor.
 */
public class AnnotatedElementSupplier extends ElementSupplier {
	
}