package com.matthewtamlin.java_compiler_utilities.element_util.element_supplier;

import javax.tools.JavaFileObject;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public abstract class ElementSupplier {
	/**
	 * The JavaFileObject to get elements from.
	 */
	private final JavaFileObject source;
	
	/**
	 * Constructs a new ElementSupplier which sources elements from the supplied JavaFileObject.
	 *
	 * @param source
	 * 		the JavaFileObject to get elements from, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code source} is null
	 */
	public ElementSupplier(final JavaFileObject source) {
		this.source = checkNotNull(source, "Argument \'source\' cannot be null.");
	}
	
	public JavaFileObject getSource() {
		return source;
	}
}