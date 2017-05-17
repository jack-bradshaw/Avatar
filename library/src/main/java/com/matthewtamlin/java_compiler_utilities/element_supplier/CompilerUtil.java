package com.matthewtamlin.java_compiler_utilities.element_supplier;

import com.google.common.collect.ImmutableSet;
import com.matthewtamlin.java_compiler_utilities.collectors.ElementCollector;
import com.matthewtamlin.java_compiler_utilities.in_memory_file_utils.InMemoryJavaFileManager;

import javax.tools.*;
import java.util.Locale;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

public class CompilerUtil {
	/**
	 * The exception to throw if there is no Java compiler available at runtime.
	 */
	private static final CompilerMissingException NO_COMPILER_EXCEPTION = new CompilerMissingException(
			"Cannot get elements if there is no Java compiler available at runtime.");
	
	/**
	 * Compiles the supplied Java file object using the system Java compiler and the supplied collector. All
	 * generated files are stored in memory and will not be written to the disk.
	 *
	 * @param javaFileObject
	 * 		the java file object to compile, not null
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
	public static void compileUsingCollector(
			final JavaFileObject javaFileObject,
			final ElementCollector<?> collector)
			throws CompilerMissingException {
		
		checkNotNull(javaFileObject, "Argument \'javaFileObject\' cannot be null.");
		checkNotNull(javaFileObject, "Argument \'collector\' cannot be null.");
		
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
				ImmutableSet.of(javaFileObject));
		
		task.setProcessors(ImmutableSet.of(collector));
		task.call();
	}
}