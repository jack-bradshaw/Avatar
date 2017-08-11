package com.matthewtamlin.avatar.compilation;

import com.google.common.collect.ImmutableSet;

import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.Arrays;
import java.util.Locale;

import static com.matthewtamlin.avatar.util.IterableNullChecker.checkNotContainsNull;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Compiles Java source files using an annotation processors. All generated files are stored in memory.
 */
public class CompilerUtil {
	/**
	 * Compiles the supplied sources with the system Java compiler and the supplied processor. All generated files are
	 * stored in memory.
	 *
	 * @param processor
	 * 		the processor to use when compiling, not null
	 * @param sources
	 * 		the sources to compile, not null, not containing null (if an array is supplied)
	 *
	 * @throws CompilerMissingException
	 * 		if no Java compiler is found at runtime
	 * @throws IllegalArgumentException
	 * 		if {@code processor} is null
	 * @throws IllegalArgumentException
	 * 		if {@code sources} is null
	 * @throws IllegalArgumentException
	 * 		if (@code sources} is an array which contains null
	 */
	public static CompilationResult compileUsingProcessor(final Processor processor, final JavaFileObject... sources) {
		return compileUsingProcessor(processor, Arrays.asList(sources));
	}
	
	/**
	 * Compiles the supplied sources with the system Java compiler and the supplied processor. All generated files are
	 * stored in memory.
	 *
	 * @param processor
	 * 		the processor to use when compiling, not null
	 * @param sources
	 * 		the sources to compile, not null, not containing null
	 *
	 * @throws CompilerMissingException
	 * 		if no Java compiler is found at runtime
	 * @throws IllegalArgumentException
	 * 		if {@code processor} is null
	 * @throws IllegalArgumentException
	 * 		if {@code sources} is null
	 * @throws IllegalArgumentException
	 * 		if (@code sources} contains null
	 */
	public static CompilationResult compileUsingProcessor(
			final Processor processor,
			final Iterable<JavaFileObject> sources) {
		
		checkNotNull(processor, "Argument \'processor\' cannot be null.");
		checkNotNull(sources, "Argument \'sources\' cannot be null.");
		checkNotContainsNull(sources, "Argument \'sources\' cannot contain null.");
		
		final JavaCompiler compiler = checkNotNull(
				ToolProvider.getSystemJavaCompiler(),
				new CompilerMissingException("Cannot get elements if there is no Java compiler available at runtime."));
		
		final DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<>();
		final JavaFileManager baseFileManager = compiler.getStandardFileManager(diagnostic, Locale.getDefault(), UTF_8);
		final InMemoryJavaFileManager inMemoryFileManager = new InMemoryJavaFileManager(baseFileManager);
		
		final JavaCompiler.CompilationTask task = compiler.getTask(
				null,
				inMemoryFileManager,
				diagnostic,
				null,
				null,
				ImmutableSet.copyOf(sources));
		
		task.setProcessors(ImmutableSet.of(processor));
		
		final boolean success = task.call();
		
		return CompilationResult.create(success, diagnostic.getDiagnostics(), inMemoryFileManager.getOutputFiles());
	}
}