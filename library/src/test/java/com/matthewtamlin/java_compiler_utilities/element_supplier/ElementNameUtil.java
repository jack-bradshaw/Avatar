package com.matthewtamlin.java_compiler_utilities.element_supplier;

import javax.lang.model.element.Element;
import java.util.HashSet;
import java.util.Set;

public class ElementNameUtil {
	public static Set<String> getElementNames(final Set<Element> elements) {
		final Set<String> names = new HashSet<>();
		
		for (final Element e : elements) {
			names.add(e.getSimpleName().toString());
		}
		
		return names;
	}
}