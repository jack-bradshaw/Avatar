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
 * A utility for compiling java source code. All generated files are stored in memory.
 */
public class CompilerUtil {
	/**
	 * The exception to throw if there is no Java compiler available at runtime.
	 */
	private static final CompilerMissingException NO_COMPILER_EXCEPTION = new CompilerMissingException(
			"Cannot get elements if there is no Java compiler available at runtime.");
	
	/**
	 * Compiles the supplied sources using the supplied processor. All generated files are stored in memory and will
	 * not
	 * be written to persistent storage.
	 *
	 * @param processor
	 * 		the processor to use when compiling, not null
	 * @param sources
	 * 		the sources to compile, not null
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
	public static CompilationResult compileUsingProcessor(final Processor processor, final JavaFileObject... sources) {
		return compileUsingProcessor(processor, Arrays.asList(sources));
	}
	
	/**
	 * Compiles the supplied sources using the supplied processor. All generated files are stored in memory and will
	 * not
	 * be written to persistent storage.
	 *
	 * @param processor
	 * 		the processor to use when compiling, not null
	 * @param sources
	 * 		the sources to compile, not null
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
	public static CompilationResult compileUsingProcessor(final Processor processor, Iterable<JavaFileObject>
			sources) {
		checkNotNull(processor, "Argument \'processor\' cannot be null.");
		checkNotNull(sources, "Argument \'sources\' cannot be null.");
		checkNotContainsNull(sources, "Argument \'sources\' cannot contain null.");
		
		final JavaCompiler compiler = checkNotNull(ToolProvider.getSystemJavaCompiler(), NO_COMPILER_EXCEPTION);
		
		final DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<>();
		final JavaFileManager baseFileManager = compiler.getStandardFileManager(diagnostic, Locale.getDefault(),
				UTF_8);
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