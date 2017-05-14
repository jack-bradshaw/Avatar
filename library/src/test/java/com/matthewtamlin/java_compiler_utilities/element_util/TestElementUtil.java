package com.matthewtamlin.java_compiler_utilities.element_util;

import com.google.common.collect.ImmutableSet;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Automated tests for the {@link ElementUtil} class.
 */
@RunWith(JUnit4.class)
public class TestElementUtil {
	private static final File NORMAL_JAVA_FILE = new File("src/test/java/com/matthewtamlin/" +
			"java_compiler_utilities/element_util/NormalJavaFile.java");
	
	private static final File EMPTY_JAVA_FILE = new File("src/test/java/com/matthewtamlin/" +
			"java_compiler_utilities/element_util/EmptyJavaFile.java");
	
	private JavaFileObject normalJavaFileObject;
	
	private JavaFileObject emptyJavaFileObject;
	
	@Before
	public void setup() throws MalformedURLException {
		assertThat("Normal Java file does not exist.", NORMAL_JAVA_FILE.exists(), is(true));
		assertThat("Empty Java file does not exist.", EMPTY_JAVA_FILE.exists(), is(true));
		
		normalJavaFileObject = JavaFileObjects.forResource(NORMAL_JAVA_FILE.toURI().toURL());
		emptyJavaFileObject = JavaFileObjects.forResource(EMPTY_JAVA_FILE.toURI().toURL());
		
		assertThat("Normal Java file object does not exist.", normalJavaFileObject, is(notNullValue()));
		assertThat("Empty Java file object does not exist.", emptyJavaFileObject, is(notNullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetRootElementsFrom_nullFile() throws CompilerMissingException {
		ElementUtil.getRootElements(null);
	}
	
	@Test
	public void testGetRootElementsFrom_normalJavaFile() throws CompilerMissingException {
		final Set<Element> elements = ElementUtil.getRootElements(normalJavaFileObject);
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("Incorrect elements returned.", toElementNames(elements), is(getRootElementNames()));
	}
	
	@Test
	public void testGetRootElementsFrom_emptyJavaFile() throws CompilerMissingException {
		final Set<Element> elements = ElementUtil.getRootElements(emptyJavaFileObject);
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("No elements should have been returned.", elements.isEmpty(), is(true));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetTaggedElementsFrom_nullFile() throws CompilerMissingException {
		final Set<Class<? extends Annotation>> tags = new HashSet<>();
		tags.add(Tag1.class);
		
		ElementUtil.getTaggedElements(null, tags);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetTaggedElementsFrom_nullTags() throws CompilerMissingException {
		ElementUtil.getTaggedElements(normalJavaFileObject, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetTaggedElementsFrom_tagsContainsNull() throws CompilerMissingException {
		final Set<Class<? extends Annotation>> tags = new HashSet<>();
		tags.add(Tag1.class);
		tags.add(null);
		
		ElementUtil.getTaggedElements(normalJavaFileObject, tags);
	}
	
	@Test
	public void testGetTaggedElementsFrom_normalJavaFile_noTags() throws CompilerMissingException {
		final Set<Class<? extends Annotation>> tags = new HashSet<>();
		
		final Set<Element> elements = ElementUtil.getTaggedElements(normalJavaFileObject, tags);
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("No elements should have been returned.", elements.isEmpty(), is(true));
	}
	
	@Test
	public void testGetTaggedElementsFrom_normalJavaFile_tag1() throws CompilerMissingException {
		final Set<Class<? extends Annotation>> tags = new HashSet<>();
		tags.add(Tag1.class);
		
		final Set<Element> elements = ElementUtil.getTaggedElements(normalJavaFileObject, tags);
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("Incorrect elements returned.", toElementNames(elements), is(getTag1ElementNames()));
	}
	
	@Test
	public void testGetTaggedElementsFrom_normalJavaFile_bothTags() throws CompilerMissingException {
		final Set<Class<? extends Annotation>> tags = new HashSet<>();
		tags.add(Tag1.class);
		tags.add(Tag2.class);
		
		final Set<Element> elements = ElementUtil.getTaggedElements(normalJavaFileObject, tags);
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("Incorrect elements returned.", toElementNames(elements), is(getBothTagsElementNames()));
	}
	
	@Test
	public void testGetTaggedElementsFrom_emptyJavaFile_noTags() throws CompilerMissingException {
		final Set<Class<? extends Annotation>> tags = new HashSet<>();
		
		final Set<Element> elements = ElementUtil.getTaggedElements(emptyJavaFileObject, tags);
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("No elements should have been returned.", elements.isEmpty(), is(true));
	}
	
	@Test
	public void testGetTaggedElementsFrom_emptyJavaFile_tag1() throws CompilerMissingException {
		final Set<Class<? extends Annotation>> tags = new HashSet<>();
		tags.add(Tag1.class);
		
		final Set<Element> elements = ElementUtil.getTaggedElements(emptyJavaFileObject, tags);
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("No elements should have been returned.", elements.isEmpty(), is(true));
	}
	
	@Test
	public void testGetTaggedElementsFrom_emptyJavaFile_bothTags() throws CompilerMissingException {
		final Set<Class<? extends Annotation>> tags = new HashSet<>();
		tags.add(Tag1.class);
		tags.add(Tag2.class);
		
		final Set<Element> elements = ElementUtil.getTaggedElements(emptyJavaFileObject, tags);
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("No elements should have been returned.", elements.isEmpty(), is(true));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetElementsByIdFrom_nullFile() throws CompilerMissingException {
		ElementUtil.getElementsById(null, "something");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetElementsByIdFrom_nullId() throws CompilerMissingException {
		ElementUtil.getElementsById(normalJavaFileObject, null);
	}
	
	@Test
	public void testGetElementsByIdFrom_normalFile_noElementsFoundForId() throws CompilerMissingException {
		final Set<Element> elements = ElementUtil.getElementsById(normalJavaFileObject, "nothing");
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("No elements should have been returned.", elements.isEmpty(), is(true));
	}
	
	@Test
	public void testGetElementsByIdFrom_normalFile_oneElementFoundForId() throws CompilerMissingException {
		final Set<Element> elements = ElementUtil.getElementsById(normalJavaFileObject, "1");
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("Incorrect elements returned.", toElementNames(elements), is(getId1ElementNames()));
	}
	
	@Test
	public void testGetElementsByIdFrom_normalFile_multipleElementsFoundForId() throws CompilerMissingException {
		final Set<Element> elements = ElementUtil.getElementsById(normalJavaFileObject, "2");
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("Incorrect elements returned.", toElementNames(elements), is(getId2ElementNames()));
	}
	
	@Test
	public void testGetElementsByIdFrom_emptyFile() throws CompilerMissingException {
		final Set<Element> elements = ElementUtil.getElementsById(emptyJavaFileObject, "2");
		
		assertThat("Returned collection should never be null.", elements, is(notNullValue()));
		assertThat("No elements should have been returned.", elements.isEmpty(), is(true));
	}
	
	private Set<String> toElementNames(Set<Element> elements) {
		final Set<String> names = new HashSet<>();
		
		for (final Element e : elements) {
			names.add(e.getSimpleName().toString());
		}
		
		return ImmutableSet.copyOf(names);
	}
	
	private Set<String> getRootElementNames() {
		return ImmutableSet.of(
				"NormalJavaFile",
				"DefaultClassWithTag1",
				"DefaultClassWithTag2",
				"DefaultClassWithoutTag");
	}
	
	private Set<String> getTag1ElementNames() {
		return ImmutableSet.of(
				"constantWithTag1",
				"fieldWithTag1",
				"methodWithTag1",
				"innerClassWithTag1",
				"parameterWithTag1",
				"DefaultClassWithTag1");
	}
	
	private Set<String> getTag2ElementNames() {
		return ImmutableSet.of(
				"constantWithTag2",
				"fieldWithTag2",
				"methodWithTag2",
				"innerClassWithTag2",
				"parameterWithTag2",
				"DefaultClassWithTag2");
	}
	
	private Set<String> getBothTagsElementNames() {
		final Set<String> combinedNames = new HashSet<>();
		
		combinedNames.addAll(getTag1ElementNames());
		combinedNames.addAll(getTag2ElementNames());
		
		return ImmutableSet.copyOf(combinedNames);
	}
	
	public Set<String> getId1ElementNames() {
		return ImmutableSet.of("methodWithId1");
	}
	
	public Set<String> getId2ElementNames() {
		return ImmutableSet.of("methodWithId2", "fieldWithId2");
	}
}