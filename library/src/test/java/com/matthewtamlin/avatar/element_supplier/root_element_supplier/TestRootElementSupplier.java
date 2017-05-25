package com.matthewtamlin.avatar.element_supplier.root_element_supplier;

import com.google.common.collect.ImmutableSet;
import com.google.testing.compile.JavaFileObjects;
import com.matthewtamlin.avatar.element_supplier.RootElementSupplier;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Set;

import static com.matthewtamlin.avatar.element_supplier.testing_utils.ElementNameUtil.getElementNames;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link RootElementSupplier} class.
 */
public class TestRootElementSupplier {
	private static final Set<String> ROOT_NAMES = ImmutableSet.of("NormalSourceFile", "OtherClass");
	
	private static final File EMPTY_FILE = new File("src/test/java/com/matthewtamlin/avatar/" +
			"element_supplier/root_element_supplier/EmptySourceFile.java");
	
	private static final File NORMAL_FILE = new File("src/test/java/com/matthewtamlin/avatar/" +
			"element_supplier/root_element_supplier/NormalSourceFile.java");
	
	private JavaFileObject emptyJavaFileObject;
	
	private JavaFileObject normalJavaFileObject;
	
	@Before
	public void setup() throws MalformedURLException {
		assertThat("Empty file does not exist.", EMPTY_FILE.exists(), is(true));
		assertThat("Normal file does not exist.", NORMAL_FILE.exists(), is(true));
		
		emptyJavaFileObject = JavaFileObjects.forResource(EMPTY_FILE.toURI().toURL());
		normalJavaFileObject = JavaFileObjects.forResource(NORMAL_FILE.toURI().toURL());
		
		assertThat("Empty java file object is null.", emptyJavaFileObject, is(notNullValue()));
		assertThat("Normal java file object is null.", normalJavaFileObject, is(notNullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullFile() {
		new RootElementSupplier(null);
	}
	
	@Test
	public void testGetElementsWithAnnotation_normalFile() {
		final RootElementSupplier supplier = new RootElementSupplier(normalJavaFileObject);
		
		final Set<Element> elements = supplier.getRootElements();
		
		assertThat("Returned set should never be null.", elements, is(notNullValue()));
		assertThat("Returned set did not match expected set.", getElementNames(elements), is(ROOT_NAMES));
	}
	
	@Test
	public void testGetElementsWithAnnotation_emptyFile() {
		final RootElementSupplier supplier = new RootElementSupplier(emptyJavaFileObject);
		
		final Set<Element> elements = supplier.getRootElements();
		
		assertThat("Returned set should never be null.", elements, is(notNullValue()));
		assertThat("Returned set should be empty.", elements.isEmpty(), is(true));
	}
}