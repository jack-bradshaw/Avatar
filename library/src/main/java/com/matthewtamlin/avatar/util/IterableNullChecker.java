package com.matthewtamlin.avatar.util;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class IterableNullChecker {
	public static <T> Iterable<T> checkNotContainsNull(final Iterable<T> iterable, final String errorMessage) {
		checkNotNull(iterable, "Argument \'iterable\' cannot be null.");
		
		for (final Object o : iterable) {
			checkNotNull(o, errorMessage);
		}
		
		return iterable;
	}
}