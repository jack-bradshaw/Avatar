package com.matthewtamlin.avatar.rules;

/**
 * Exception to indicates that a unique element for some criteria could not be found when searching a source file.
 */
public class UniqueElementNotFoundException extends RuntimeException {
	/**
	 * Constructs a new UniqueElementNotFoundException with no message and no cause.
	 */
	public UniqueElementNotFoundException() {
		super();
	}
	
	/**
	 * Constructs a new UniqueElementNotFoundException with a message but no cause.
	 */
	public UniqueElementNotFoundException(final String message) {
		super(message);
	}
	
	/**
	 * Constructs a new UniqueElementNotFoundException with a message and a cause.
	 */
	public UniqueElementNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a new UniqueElementNotFoundException with a cause but no message.
	 */
	public UniqueElementNotFoundException(final Throwable cause) {
		super(cause);
	}
}