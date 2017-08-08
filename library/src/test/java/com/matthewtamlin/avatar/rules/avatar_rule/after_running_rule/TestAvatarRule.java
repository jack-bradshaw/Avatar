package com.matthewtamlin.avatar.rules.avatar_rule.after_running_rule;

import com.matthewtamlin.avatar.rules.AvatarRule;
import com.matthewtamlin.avatar.rules.UniqueElementNotFoundException;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.lang.model.element.Element;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class TestAvatarRule {
	@Rule
	public final AvatarRule rule = new AvatarRule(
			"src/test/java/com/matthewtamlin/avatar/rules/avatar_rule/after_running_rule/Data.java");
	
	@Test
	public void testGetProcessingEnvironment_checkReturnIsNotNull() {
		assertThat(
				"Processing environment must not be null.",
				rule.getProcessingEnvironment(),
				is(notNullValue()));
	}
	
	@Test
	public void testGetCompilationResult_checkReturnIsNotNull() {
		assertThat(
				"Compilation result must not be null.",
				rule.getCompilationResult(),
				is(CoreMatchers.notNullValue()));
	}
	
	@Test
	public void testGetRoundEnvironments_checkReturnIsNotNull() {
		assertThat(
				"Round environments must not be null.",
				rule.getRoundEnvironments(),
				is(notNullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetElementsWithId_nullId() {
		rule.getElementsWithId(null);
	}
	
	@Test
	public void testGetElementsWithId_idMatchesNoElements() {
		final Set<Element> elements = rule.getElementsWithId("ID0");
		
		assertThat("Element set must not be null.", elements, is(notNullValue()));
		assertThat("Element set must be empty.", elements.isEmpty(), is(true));
	}
	
	@Test
	public void testGetElementsWithId_idMatchesOneElement() {
		final Set<Element> elements = rule.getElementsWithId("ID1");
		
		assertThat("Element set must not be null.", elements, is(notNullValue()));
		assertThat("Element set must contain exactly one item.", elements.size(), is(1));
		assertThat("Element set must contain methodB.", containsByName(elements, "methodB"));
	}
	
	@Test
	public void testGetElementsWithId_idMatchesMultipleElements() {
		final Set<Element> elements = rule.getElementsWithId("ID2");
		
		assertThat("Element set must not be null.", elements, is(notNullValue()));
		assertThat("Element set must contain exactly two items.", elements.size(), is(2));
		assertThat("Element set must contain fieldA.", containsByName(elements, "fieldA"));
		assertThat("Element set must contain fieldB.", containsByName(elements, "fieldB"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetElementWithUniqueId_nullId() {
		rule.getElementWithUniqueId(null);
	}
	
	@Test(expected = UniqueElementNotFoundException.class)
	public void testGetElementWithUniqueId_idMatchesNoElements() {
		rule.getElementWithUniqueId("ID0");
	}
	
	@Test
	public void testGetElementWithUniqueId_idMatchesOneElement() {
		final Element element = rule.getElementWithUniqueId("ID1");
		
		assertThat("Element must not be null.", element, is(notNullValue()));
		assertThat("Element must be methodB.", element.getSimpleName().toString(), is("methodB"));
	}
	
	@Test(expected = UniqueElementNotFoundException.class)
	public void testGetElementWithUniqueId_idMatchesMultipleElements() {
		rule.getElementWithUniqueId("ID2");
	}
	
	@Test
	public void testGetElementWithUniqueId_canFindClasses() {
		final Element element = rule.getElementWithUniqueId("class");
		
		assertThat("Element must not be null.", element, is(notNullValue()));
		assertThat("Element must be ClassA.", element.getSimpleName().toString(), is("ClassA"));
	}
	
	@Test
	public void testGetElementWithUniqueId_canFindInterfaces() {
		final Element element = rule.getElementWithUniqueId("interface");
		
		assertThat("Element must not be null.", element, is(notNullValue()));
		assertThat("Element must be InterfaceA.", element.getSimpleName().toString(), is("InterfaceA"));
	}
	
	@Test
	public void testGetElementWithUniqueId_canFindMethods() {
		final Element element = rule.getElementWithUniqueId("method");
		
		assertThat("Element must not be null.", element, is(notNullValue()));
		assertThat("Element must be methodC.", element.getSimpleName().toString(), is("methodC"));
	}
	
	@Test
	public void testGetElementWithUniqueId_canFindFields() {
		final Element element = rule.getElementWithUniqueId("field");
		
		assertThat("Element must not be null.", element, is(notNullValue()));
		assertThat("Element must be fieldC.", element.getSimpleName().toString(), is("fieldC"));
	}
	
	@Test
	public void testGetElementWithUniqueId_canFindMethodParameters() {
		final Element element = rule.getElementWithUniqueId("parameter");
		
		assertThat("Element must not be null.", element, is(notNullValue()));
		assertThat("Element must be parameterA.", element.getSimpleName().toString(), is("parameterA"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetElementsWithAnnotation_nullAnnotation() {
		rule.getElementsWithAnnotation(null);
	}
	
	@Test
	public void testGetElementsWithAnnotation_annotationMatchesNoElements() {
		final Set<Element> elements = rule.getElementsWithAnnotation(Annotation0.class);
		
		assertThat("Element set must not be null.", elements, is(notNullValue()));
		assertThat("Element set must be empty.", elements.isEmpty(), is(true));
	}
	
	@Test
	public void testGetElementsWithAnnotation_annotationMatchesOneElement() {
		final Set<Element> elements = rule.getElementsWithAnnotation(Annotation1.class);
		
		assertThat("Element set must not be null.", elements, is(notNullValue()));
		assertThat("Element set must contain exactly one item.", elements.size(), is(1));
		assertThat("Element set must contain methodB.", containsByName(elements, "methodB"));
	}
	
	@Test
	public void testGetElementsWithAnnotation_annotationMatchesMultipleElements() {
		final Set<Element> elements = rule.getElementsWithAnnotation(Annotation2.class);
		
		assertThat("Element set must not be null.", elements, is(notNullValue()));
		assertThat("Element set must contain exactly two items.", elements.size(), is(2));
		assertThat("Element set must contain fieldA.", containsByName(elements, "fieldA"));
		assertThat("Element set must contain fieldB.", containsByName(elements, "fieldB"));
	}
	
	@Test
	public void testGetRootElements() {
		final Set<Element> elements = rule.getRootElements();
		
		assertThat("Element set must not be null.", elements, is(notNullValue()));
		assertThat("Element set must contain exactly one item.", elements.size(), is(1));
		assertThat("Element set must contain Data.", containsByName(elements, "Data"));
	}
	
	private boolean containsByName(final Set<? extends Element> elements, final String name) {
		for (final Element e : elements) {
			if (e.getSimpleName().toString().equals(name)) {
				return true;
			}
		}
		
		return false;
	}
}