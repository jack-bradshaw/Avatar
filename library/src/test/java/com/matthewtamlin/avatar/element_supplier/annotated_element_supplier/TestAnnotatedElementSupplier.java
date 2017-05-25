package com.matthewtamlin.avatar.element_supplier.annotated_element_supplier;

import com.google.common.collect.ImmutableSet;
import com.google.testing.compile.JavaFileObjects;
import com.matthewtamlin.avatar.element_supplier.AnnotatedElementSupplier;
import com.matthewtamlin.avatar.element_supplier.CompilerMissingException;
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
 * Unit tests for the {@link AnnotatedElementSupplier} class.
 */
public class TestAnnotatedElementSupplier {
	private static final Set<String> USED_ONCE_NAMES = ImmutableSet.of("field1");
	
	private static final Set<String> USED_TWICE_NAMES = ImmutableSet.of("method1", "method2");
	
	private static final File EMPTY_FILE = new File("src/test/java/com/matthewtamlin/avatar/" +
			"element_supplier/annotated_element_supplier/EmptySourceFile.java");
	
	private static final File NORMAL_FILE = new File("src/test/java/com/matthewtamlin/avatar/" +
			"element_supplier/annotated_element_supplier/NormalSourceFile.java");
	
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
		new AnnotatedElementSupplier(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetElementsWithAnnotation_nullAnnotation() throws CompilerMissingException {
		final AnnotatedElementSupplier supplier = new AnnotatedElementSupplier(normalJavaFileObject);
		
		supplier.getElementsWithAnnotation(null);
	}
	
	@Test
	public void testGetElementsWithAnnotation_elementIsNotFound() throws CompilerMissingException {
		final AnnotatedElementSupplier supplier = new AnnotatedElementSupplier(normalJavaFileObject);
		
		final Set<Element> elements = supplier.getElementsWithAnnotation(UnusedAnnotation.class);
		
		assertThat("Returned set should never be null.", elements, is(notNullValue()));
		assertThat("Returned set should be empty.", elements.isEmpty(), is(true));
	}
	
	@Test
	public void testGetElementsWithAnnotation_elementIsFoundOnce() throws CompilerMissingException {
		final AnnotatedElementSupplier supplier = new AnnotatedElementSupplier(normalJavaFileObject);
		
		final Set<Element> elements = supplier.getElementsWithAnnotation(UsedOnceAnnotation.class);
		
		assertThat("Returned set should never be null.", elements, is(notNullValue()));
		assertThat("Returned set did not match expected set.", getElementNames(elements), is(USED_ONCE_NAMES));
	}
	
	@Test
	public void testGetElementsWithAnnotation_elementIsFoundMultipleTimes() throws CompilerMissingException {
		final AnnotatedElementSupplier supplier = new AnnotatedElementSupplier(normalJavaFileObject);
		
		final Set<Element> elements = supplier.getElementsWithAnnotation(UsedTwiceAnnotation.class);
		
		assertThat("Returned set should never be null.", elements, is(notNullValue()));
		assertThat("Returned set did not match expected set.", getElementNames(elements), is(USED_TWICE_NAMES));
	}
	
	@Test
	public void testGetElementsWithAnnotation_fileIsEmpty() throws CompilerMissingException {
		final AnnotatedElementSupplier supplier = new AnnotatedElementSupplier(emptyJavaFileObject);
		
		final Set<Element> elements = supplier.getElementsWithAnnotation(UsedOnceAnnotation.class);
		
		assertThat("Returned set should never be null.", elements, is(notNullValue()));
		assertThat("Returned set should be empty.", elements.isEmpty(), is(true));
	}
}