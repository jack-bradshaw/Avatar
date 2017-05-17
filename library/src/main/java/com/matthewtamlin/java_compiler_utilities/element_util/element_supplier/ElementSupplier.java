package com.matthewtamlin.java_compiler_utilities.element_util.element_supplier;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.util.Collection;

public interface ElementSupplier<T extends Collection<? extends Element>> {
	public T getFrom(final JavaFileObject source);
}