package com.matthewtamlin.avatar.compilation;

import javax.tools.JavaCompiler;

/**
 * Exception that indicates the absence of a {@link JavaCompiler} at runtime.
 */
public class CompilerMissingException extends RuntimeException {
	/**
	 * Constructs a new compiler missing exception with no message and no cause.
	 */
	public CompilerMissingException() {
		super();
	}
	
	/**
	 * Constructs a new compiler missing exception with a message but no cause.
	 */
	public CompilerMissingException(final String message) {
		super(message);
	}
	
	/**
	 * Constructs a new compiler missing exception with a message and a cause.
	 */
	public CompilerMissingException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a new compiler missing exception with a cause but no message.
	 */
	public CompilerMissingException(final Throwable cause) {
		super(cause);
	}
}