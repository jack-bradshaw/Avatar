package com.matthewtamlin.avatar.rules;

/**
 * Exception which indicates that a unique element for some criteria could not be found when searching a source file.
 */
public class UniqueElementNotFoundException extends RuntimeException {
	public UniqueElementNotFoundException() {
		super();
	}
	
	public UniqueElementNotFoundException(final String message) {
		super(message);
	}
	
	public UniqueElementNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	public UniqueElementNotFoundException(final Throwable cause) {
		super(cause);
	}
}