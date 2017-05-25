package com.matthewtamlin.avatar.element_supplier;

import com.matthewtamlin.avatar.collectors.ElementCollector;
import com.matthewtamlin.avatar.collectors.IdBasedElementCollector;
import com.matthewtamlin.java_utilities.testing.Tested;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * Gets specific elements from a {@link JavaFileObject} via {@link ElementId} annotations in the source code.
 */
@Tested(testMethod = "automated")
public class IdBasedElementSupplier {
	/**
	 * The source to get elements from.
	 */
	private JavaFileObject source;
	
	/**
	 * Constructs a new IdBasedElementSupplier.
	 *
	 * @param source
	 * 		the JavaFileObject to get elements from, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code source} is null
	 */
	public IdBasedElementSupplier(final JavaFileObject source) {
		this.source = checkNotNull(source, "Argument \'source\' cannot be null.");
	}
	
	/**
	 * Gets all elements from the source which have an {@link ElementId} annotation with the supplied ID. This method
	 * might return an empty set, but it will never return null.
	 *
	 * @param id
	 * 		the ID to search for, not null
	 *
	 * @return all elements found in the source with the supplied ID, not null
	 *
	 * @throws CompilerMissingException
	 * 		if there is no Java compiler available at runtime
	 * @throws IllegalArgumentException
	 * 		if {@code id} is null
	 */
	public Set<Element> getElementsWithId(final String id) throws CompilerMissingException {
		checkNotNull(id, "Argument \'id\' cannot be null.");
		
		final ElementCollector<Set<Element>> collector = new IdBasedElementCollector(id);
		
		CompilerUtil.compileUsingCollector(source, collector);
		
		return collector.getCollectedElements();
	}
	
	/**
	 * Gets a single element from the source by searching for an element which has an {@link ElementId} annotation with
	 * the supplied ID. This method expects to find exactly one such element, and will throw an exception if none or
	 * multiple are found.
	 *
	 * @param id
	 * 		the id to search for, not null
	 *
	 * @return the element found in the source with the supplied ID
	 *
	 * @throws CompilerMissingException
	 * 		if there is no Java compiler available at runtime
	 * @throws IllegalArgumentException
	 * 		if {@code id} is null
	 * @throws UniqueElementNotFoundException
	 * 		if no element is found with the supplied ID
	 * @throws UniqueElementNotFoundException
	 * 		if multiple elements are found with the supplied ID
	 */
	public Element getUniqueElementWithId(final String id) throws CompilerMissingException {
		checkNotNull(id, "Argument \'id\' cannot be null.");
		
		final Set<Element> elements = getElementsWithId(id);
		
		if (elements.isEmpty()) {
			throw new UniqueElementNotFoundException("No elements found for ID: " + id);
		}
		
		if (elements.size() > 1) {
			throw new UniqueElementNotFoundException("Multiple elements found for ID: " + id);
		}
		
		return elements.iterator().next();
	}
}