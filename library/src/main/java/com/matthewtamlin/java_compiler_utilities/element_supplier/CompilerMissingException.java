package com.matthewtamlin.java_compiler_utilities.element_supplier;

import javax.tools.JavaCompiler;

/**
 * Exception indicating the absence of a {@link JavaCompiler} at runtime.
 */
public class CompilerMissingException extends Exception {
	public CompilerMissingException() {
		super();
	}
	
	public CompilerMissingException(final String message) {
		super(message);
	}
	
	public CompilerMissingException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public CompilerMissingException(final Throwable cause) {
		super(cause);
	}
}