package com.matthewtamlin.avatar.compilation;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import java.util.ArrayList;
import java.util.List;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class DiagnosticCollector<T> implements DiagnosticListener<T> {
	private List<Diagnostic<? extends T>> diagnostics = new ArrayList<>();
	
	@Override
	public void report(final Diagnostic<? extends T> diagnostic) {
		checkNotNull(diagnostic, "Argument \'diagnostic\' cannot be null.");
		diagnostics.add(diagnostic);
	}
	
	public List<Diagnostic<? extends T>> getDiagnostics() {
		return diagnostics;
	}
}