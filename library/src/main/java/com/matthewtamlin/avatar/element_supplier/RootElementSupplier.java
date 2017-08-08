package com.matthewtamlin.avatar.element_supplier;

import com.matthewtamlin.avatar.collectors.ElementCollector;
import com.matthewtamlin.avatar.collectors.RootElementCollector;
import com.matthewtamlin.avatar.compilation.CompilerUtil;
import com.matthewtamlin.java_utilities.testing.Tested;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Gets all root elements from a {@link JavaFileObject}.
 *
 * @deprecated use {@link AvatarRule}
 */
@Deprecated
@Tested(testMethod = "automated")
public class RootElementSupplier {
	/**
	 * The source to get elements from.
	 */
	private JavaFileObject source;
	
	/**
	 * Constructs a new RootElementSupplier.
	 *
	 * @param source
	 * 		the JavaFileObject to get elements from, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code source} is null
	 */
	public RootElementSupplier(final JavaFileObject source) {
		this.source = checkNotNull(source, "Argument \'source\' cannot be null.");
	}
	
	/**
	 * Gets all root elements from the source. This method might return an empty set, but it will never return null.
	 *
	 * @return all root elements found in the source, not null
	 *
	 * @throws CompilerMissingException
	 * 		if there is no Java compiler available at runtime
	 */
	public Set<Element> getRootElements() {
		final ElementCollector<Set<Element>> collector = new RootElementCollector();
		
		CompilerUtil.compileUsingProcessor(source, collector);
		
		return collector.getCollectedElements();
	}
}