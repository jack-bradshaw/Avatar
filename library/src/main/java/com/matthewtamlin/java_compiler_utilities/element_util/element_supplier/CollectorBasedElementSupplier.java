package com.matthewtamlin.java_compiler_utilities.element_util.element_supplier;

import com.google.common.collect.ImmutableSet;
import com.matthewtamlin.java_compiler_utilities.element_util.collectors.ElementCollector;
import com.matthewtamlin.java_compiler_utilities.element_util.in_memory_file_utils.InMemoryJavaFileManager;

import javax.lang.model.element.Element;
import javax.tools.*;
import java.util.Collection;
import java.util.Locale;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * An element supplier which uses an {@link ElementCollector} to find elements in source files.
 */
public abstract class CollectorBasedElementSupplier<T extends Collection<? extends Element>>
		implements ElementSupplier<T> {
	
	/**
	 * The exception to throw if there is no Java compiler available at Runtime.
	 */
	private static final CompilerMissingException NO_COMPILER_EXCEPTION = new CompilerMissingException(
			"Cannot get elements if there is no Java compiler available at runtime.");
	
	/**
	 * @return a new instance of the element collector type used by this class
	 */
	protected abstract ElementCollector<T> getNewCollector();
	
	@Override
	public T getFrom(final JavaFileObject source) throws CompilerMissingException {
		checkNotNull(source, "Argument \'source\' cannot be null.");
		
		final ElementCollector<? extends T> collector = getNewCollector();
		
		compileUsingCollector(source, collector);
		
		return collector.getCollectedElements();
	}
	
	/**
	 * Compiles the supplied Java file object using the system Java compiler and the supplied collector.
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
	private static void compileUsingCollector(
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