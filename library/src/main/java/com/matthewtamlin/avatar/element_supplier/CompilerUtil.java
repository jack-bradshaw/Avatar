package com.matthewtamlin.avatar.element_supplier;

import com.google.common.collect.ImmutableSet;
import com.matthewtamlin.avatar.collectors.ElementCollector;
import com.matthewtamlin.avatar.in_memory_file_utils.InMemoryJavaFileManager;

import javax.tools.*;
import java.util.Locale;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A utility for compiling a {@link JavaFileObject} with an {@link ElementCollector}. All generated files are stored in
 * memory and will not be written to persistent storage.
 */
public class CompilerUtil {
	/**
	 * The exception to throw if there is no Java compiler available at runtime.
	 */
	private static final CompilerMissingException NO_COMPILER_EXCEPTION = new CompilerMissingException(
			"Cannot get elements if there is no Java compiler available at runtime.");
	
	/**
	 * Compiles the supplied Java file object using the supplied collector. All generated files are stored in memory
	 * and will not be written to persistent storage.
	 *
	 * @param source
	 * 		the source to compile, not null
	 * @param collector
	 * 		the collector to use when compiling, not null
	 *
	 * @throws CompilerMissingException
	 * 		if no Java compiler is found at runtime
	 * @throws IllegalArgumentException
	 * 		if {@code javaFileObject} is null
	 * @throws IllegalArgumentException
	 * 		if {@code collector} is null
	 */
	public static void compileUsingCollector(final JavaFileObject source, final ElementCollector<?> collector) {
		checkNotNull(source, "Argument \'javaFileObject\' cannot be null.");
		checkNotNull(source, "Argument \'collector\' cannot be null.");
		
		final JavaCompiler compiler = checkNotNull(ToolProvider.getSystemJavaCompiler(), NO_COMPILER_EXCEPTION);
		final DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<>();
		
		final JavaFileManager fileManager = compiler.getStandardFileManager(diagnostic, Locale.getDefault(), UTF_8);
		final JavaFileManager inMemoryFileManager = new InMemoryJavaFileManager(fileManager);
		
		final JavaCompiler.CompilationTask task = compiler.getTask(
				null,
				inMemoryFileManager,
				diagnostic,
				null,
				null,
				ImmutableSet.of(source));
		
		task.setProcessors(ImmutableSet.of(collector));
		task.call();
	}
}