package com.matthewtamlin.avatar.rules.avatar_rule.without_running;

import com.matthewtamlin.avatar.rules.AvatarRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.tools.JavaFileObject;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;

@SuppressWarnings("RedundantArrayCreation")
@RunWith(JUnit4.class)
public class TestAvatarRule {
	private static final String DATA_FILE_PATH =
			"src/test/java/com/matthewtamlin/avatar/rules/avatar_rule/without_running/Data.java";
	
	@BeforeClass
	public static void setupClass() {
		assertThat(new File(DATA_FILE_PATH).exists(), is(true));
	}
	
	@Test
	public void testWithoutSources_checkNeverReturnsNull() {
		final AvatarRule rule = AvatarRule.withoutSources();
		
		assertThat(rule, is(notNullValue()));
	}
	
	@Test(expected = IllegalStateException.class)
	public void testInstantiateViaBuilder_noSourcesSet() {
		AvatarRule
				.builder()
				.withSuccessfulCompilationRequired(false)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_noSuccessfulCompilationRequiredFlagSet() {
		AvatarRule
				.builder()
				.withSourcesAt(DATA_FILE_PATH)
				.build();
	}
	
	
	@Test(expected = IllegalStateException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_nullIterable() {
		AvatarRule
				.builder()
				.withSourceFileObjects((Iterable<JavaFileObject>) null)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_emptyIterable() {
		final List<JavaFileObject> sources = new ArrayList<>();
		
		AvatarRule
				.builder()
				.withSourceFileObjects(sources)
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_iterableContainingNull() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(null);
		
		AvatarRule
				.builder()
				.withSourceFileObjects(sources)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_iterableContainingOneValidItem() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(mock(JavaFileObject.class));
		
		AvatarRule
				.builder()
				.withSourceFileObjects(sources)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_iterableContainingMultipleValidItems() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(mock(JavaFileObject.class));
		sources.add(mock(JavaFileObject.class));
		
		AvatarRule
				.builder()
				.withSourceFileObjects(sources)
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_nullArray() {
		AvatarRule
				.builder()
				.withSourceFileObjects((JavaFileObject) null)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_emptyArray() {
		AvatarRule
				.builder()
				.withSourceFileObjects(new JavaFileObject[]{})
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_arrayContainingNull() {
		AvatarRule
				.builder()
				.withSourceFileObjects(new JavaFileObject[]{null})
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_arrayContainingOneValidItem() {
		AvatarRule.builder().withSourceFileObjects(new JavaFileObject[]{mock(JavaFileObject.class)}).build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFileObjects_arrayContainingMultipleValidItems() {
		AvatarRule
				.builder()
				.withSourceFileObjects(mock(JavaFileObject.class), mock(JavaFileObject.class))
				.build();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_nullIterable() {
		AvatarRule
				.builder()
				.withSourceFiles((Iterable<File>) null)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_emptyIterable() {
		final List<File> sources = new ArrayList<>();
		
		AvatarRule
				.builder()
				.withSourceFiles(sources)
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_iterableContainingNull() {
		final List<File> sources = new ArrayList<>();
		sources.add(null);
		
		AvatarRule
				.builder()
				.withSourceFiles(sources)
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_iterableContainingNonExistentFile() {
		final List<File> sources = new ArrayList<>();
		sources.add(new File("I don't exist"));
		
		AvatarRule
				.builder()
				.withSourceFiles(sources)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_iterableContainingOneValidItem() {
		final List<File> sources = new ArrayList<>();
		sources.add(new File(DATA_FILE_PATH));
		
		AvatarRule
				.builder()
				.withSourceFiles(sources)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_iterableContainingMultipleValidItems() {
		final List<File> sources = new ArrayList<>();
		sources.add(new File(DATA_FILE_PATH));
		sources.add(new File(DATA_FILE_PATH));
		
		AvatarRule
				.builder()
				.withSourceFiles(sources)
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_nullArray() {
		AvatarRule
				.builder()
				.withSourceFiles((File) null)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_emptyArray() {
		AvatarRule
				.builder()
				.withSourceFiles(new File[]{})
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_arrayContainingNull() {
		AvatarRule
				.builder()
				.withSourceFiles(new File[]{null})
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_arrayContainingNonExistentFile() {
		AvatarRule
				.builder()
				.withSourceFiles(new File("I don't exist"))
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_arrayContainingOneValidItem() {
		AvatarRule
				.builder()
				.withSourceFiles(new File(DATA_FILE_PATH))
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFiles_arrayContainingMultipleValidItems() {
		AvatarRule
				.builder()
				.withSourceFiles(new File(DATA_FILE_PATH), new File(DATA_FILE_PATH))
				.build();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_nullIterable() {
		AvatarRule
				.builder()
				.withSourcesAt((Iterable<String>) null)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_emptyIterable() {
		final List<String> sources = new ArrayList<>();
		
		AvatarRule
				.builder()
				.withSourcesAt(sources)
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_iterableContainingNull() {
		final List<String> sources = new ArrayList<>();
		sources.add(null);
		
		AvatarRule
				.builder()
				.withSourcesAt(sources)
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_iterableContainingNonExistentFilePath() {
		final List<String> sources = new ArrayList<>();
		sources.add("I don't exist");
		
		AvatarRule
				.builder()
				.withSourcesAt(sources)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_iterableContainingOneValidItem() {
		final List<String> sources = new ArrayList<>();
		sources.add(DATA_FILE_PATH);
		
		AvatarRule
				.builder()
				.withSourcesAt(sources)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_iterableContainingMultipleValidItems() {
		final List<String> sources = new ArrayList<>();
		sources.add(DATA_FILE_PATH);
		sources.add(DATA_FILE_PATH);
		
		AvatarRule
				.builder()
				.withSourcesAt(sources)
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_nullValue() {
		AvatarRule
				.builder()
				.withSourcesAt((String) null)
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_emptyArray() {
		AvatarRule
				.builder()
				.withSourcesAt(new String[]{})
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_arrayContainingNull() {
		AvatarRule
				.builder()
				.withSourcesAt(new String[]{null})
				.build();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_arrayContainingNonExistentFilePath() {
		AvatarRule
				.builder()
				.withSourcesAt(new String[]{"I don't exist"})
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_arrayContainingOneValidItem() {
		AvatarRule
				.builder()
				.withSourcesAt(new String[]{DATA_FILE_PATH})
				.build();
	}
	
	@Test
	public void testInstantiateViaBuilder_sourcesSetUsingWithSourceFilesAt_arrayContainingMultipleValidItems() {
		AvatarRule
				.builder()
				.withSourcesAt(DATA_FILE_PATH, DATA_FILE_PATH)
				.build();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetProcessingEnvironment_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule
				.builder()
				.withSourcesAt(DATA_FILE_PATH)
				.build();
				
		
		rule.getProcessingEnvironment();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetCompilationResult_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule
				.builder()
				.withSourcesAt(DATA_FILE_PATH)
				.build();
		
		rule.getCompilationResult();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetRoundEnvironments_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule
				.builder()
				.withSourcesAt(DATA_FILE_PATH)
				.build();
		
		rule.getRoundEnvironments();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetElementsWithId_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule
				.builder()
				.withSourcesAt(DATA_FILE_PATH)
				.build();
		
		rule.getElementsWithId("");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetElementsWithUniqueId_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule
				.builder()
				.withSourcesAt(DATA_FILE_PATH)
				.build();
		
		rule.getElementWithUniqueId("");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetElementsWithAnnotation_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule
				.builder()
				.withSourcesAt(DATA_FILE_PATH)
				.build();
		
		rule.getElementsWithAnnotation(Annotation.class);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetRootElements_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule
				.builder()
				.withSourcesAt(DATA_FILE_PATH)
				.build();
		
		rule.getRootElements();
	}
}