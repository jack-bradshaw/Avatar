package com.matthewtamlin.avatar.compilation;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import java.util.ArrayList;
import java.util.List;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Simple utility for collecting and accessing diagnostics.
 *
 * @param <T>
 * 		the type of diagnostic to collect
 */
public class DiagnosticCollector<T> implements DiagnosticListener<T> {
	private List<Diagnostic<? extends T>> diagnostics = new ArrayList<>();
	
	@Override
	public void report(final Diagnostic<? extends T> diagnostic) {
		checkNotNull(diagnostic, "Argument \'diagnostic\' cannot be null.");
		diagnostics.add(diagnostic);
	}
	
	/**
	 * @return the collected diagnostics, may be empty, not null
	 */
	public List<Diagnostic<? extends T>> getDiagnostics() {
		return diagnostics;
	}
}