package com.matthewtamlin.avatar.compilation;

import com.google.auto.value.AutoValue;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.Collection;

@AutoValue
public abstract class CompilationResult {
	public abstract boolean success();
	
	public abstract Collection<? extends Diagnostic<? extends JavaFileObject>> diagnostics();
	
	public abstract Collection<? extends JavaFileObject> generatedFiles();
	
	public static CompilationResult create(final boolean success,
			final Collection<? extends Diagnostic<? extends JavaFileObject>> diagnostics,
			final Collection<? extends JavaFileObject> generatedFiles) {
		
		return new AutoValue_CompilationResult(success, diagnostics, generatedFiles);
	}
}