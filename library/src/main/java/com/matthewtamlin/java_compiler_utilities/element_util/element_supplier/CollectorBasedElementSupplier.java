package com.matthewtamlin.java_compiler_utilities.element_util.element_supplier;

import com.matthewtamlin.java_compiler_utilities.element_util.collectors.ElementCollector;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.util.Collection;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * An element supplier which uses an {@link ElementCollector} to find elements in source files.
 */
public abstract class CollectorBasedElementSupplier<T extends Collection<? extends Element>>
		implements ElementSupplier<T> {
	/**
	 * @return a new instance of the element collector type used by this class
	 */
	protected abstract ElementCollector<T> getNewCollector();
	
	@Override
	public T getFrom(final JavaFileObject source) throws CompilerMissingException {
		checkNotNull(source, "Argument \'source\' cannot be null.");
		
		final ElementCollector<? extends T> collector = getNewCollector();
		
		CompilerUtil.compileUsingCollector(source, collector);
		
		return collector.getCollectedElements();
	}
}