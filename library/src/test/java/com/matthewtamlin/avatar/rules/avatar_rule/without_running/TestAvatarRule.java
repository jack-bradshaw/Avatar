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

import static org.mockito.Mockito.mock;

@SuppressWarnings("RedundantArrayCreation")
@RunWith(JUnit4.class)
public class TestAvatarRule {
	private static final String DATA_FILE_PATH =
			"src/test/java/com/matthewtamlin/avatar/rules/avatar_rule/without_running/Data.java";
	
	@Test(expected = IllegalArgumentException.class)
	public void testForJavaFileObjects_iterableVariant_nullIterable() {
		AvatarRule.forJavaFileObjects((Iterable<JavaFileObject>) null);
	}
	
	@Test
	public void testForJavaFileObjects_iterableVariant_emptyIterable() {
		final List<JavaFileObject> sources = new ArrayList<>();
		
		AvatarRule.forJavaFileObjects(sources);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForJavaFileObjects_iterableVariant_iterableContainingNull() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(null);
		
		AvatarRule.forJavaFileObjects(sources);
	}
	
	@Test
	public void testForJavaFileObjects_iterableVariant_iterableContainingOneValidItem() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(mock(JavaFileObject.class));
		
		AvatarRule.forJavaFileObjects(sources);
	}
	
	@Test
	public void testForJavaFileObjects_iterableVariant_iterableContainingMultipleValidItems() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(mock(JavaFileObject.class));
		sources.add(mock(JavaFileObject.class));
		
		AvatarRule.forJavaFileObjects(sources);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForJavaFileObjects_varargVariant_nullValue() {
		AvatarRule.forJavaFileObjects((JavaFileObject) null);
	}
	
	@Test
	public void testForJavaFileObjects_varargVariant_emptyArray() {
		AvatarRule.forJavaFileObjects(new JavaFileObject[]{});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForJavaFileObjects_varargVariant_arrayContainingNull() {
		AvatarRule.forJavaFileObjects(new JavaFileObject[]{null});
	}
	
	@Test
	public void testForJavaFileObjects_varargVariant_arrayContainingOneValidItem() {
		AvatarRule.forJavaFileObjects(mock(JavaFileObject.class));
	}
	
	@Test
	public void testForJavaFileObjects_varargVariant_iterableContainingMultipleValidItems() {
		AvatarRule.forJavaFileObjects(mock(JavaFileObject.class), mock(JavaFileObject.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFiles_iterableVariant_nullIterable() {
		AvatarRule.forFiles((Iterable<File>) null);
	}
	
	@Test
	public void testForFiles_iterableVariant_emptyIterable() {
		final List<File> sources = new ArrayList<>();
		
		AvatarRule.forFiles(sources);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFiles_iterableVariant_iterableContainingNull() {
		final List<File> sources = new ArrayList<>();
		sources.add(null);
		
		AvatarRule.forFiles(sources);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFiles_iterableVariant_iterableContainingNonExistentFile() {
		final List<File> sources = new ArrayList<>();
		sources.add(new File("I don't exist"));
		
		AvatarRule.forFiles(sources);
	}
	
	@Test
	public void testForFiles_iterableVariant_iterableContainingOneValidItem() {
		final List<File> sources = new ArrayList<>();
		sources.add(new File(DATA_FILE_PATH));
		
		AvatarRule.forFiles(sources);
	}
	
	@Test
	public void testForFiles_iterableVariant_iterableContainingMultipleValidItems() {
		final List<File> sources = new ArrayList<>();
		sources.add(new File(DATA_FILE_PATH));
		sources.add(new File(DATA_FILE_PATH));
		
		AvatarRule.forFiles(sources);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFiles_varargVariant_nullValue() {
		AvatarRule.forFiles((File) null);
	}
	
	@Test
	public void testForFiles_varargVariant_emptyArray() {
		AvatarRule.forFiles(new File[]{});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFiles_varargVariant_arrayContainingNull() {
		AvatarRule.forFiles(new File[]{null});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFiles_varargVariant_arrayContainingNonExistentFile() {
		AvatarRule.forFiles(new File("I don't exist"));
	}
	
	@Test
	public void testForFiles_varargVariant_arrayContainingOneValidItem() {
		AvatarRule.forFiles(new File(DATA_FILE_PATH));
	}
	
	@Test
	public void testForFiles_varargVariant_iterableContainingMultipleValidItems() {
		AvatarRule.forFiles(new File(DATA_FILE_PATH), new File(DATA_FILE_PATH));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFilesAt_iterableVariant_nullIterable() {
		AvatarRule.forFilesAt((Iterable<String>) null);
	}
	
	@Test
	public void testForFilesAt_iterableVariant_emptyIterable() {
		final List<String> sources = new ArrayList<>();
		
		AvatarRule.forFilesAt(sources);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFilesAt_iterableVariant_iterableContainingNull() {
		final List<String> sources = new ArrayList<>();
		sources.add(null);
		
		AvatarRule.forFilesAt(sources);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFilesAt_iterableVariant_iterableContainingNonExistentFilePath() {
		final List<String> sources = new ArrayList<>();
		sources.add("I don't exist");
		
		AvatarRule.forFilesAt(sources);
	}
	
	@Test
	public void testForFilesAt_iterableVariant_iterableContainingOneValidItem() {
		final List<String> sources = new ArrayList<>();
		sources.add(DATA_FILE_PATH);
		
		AvatarRule.forFilesAt(sources);
	}
	
	@Test
	public void testForFilesAt_iterableVariant_iterableContainingMultipleValidItems() {
		final List<String> sources = new ArrayList<>();
		sources.add(DATA_FILE_PATH);
		sources.add(DATA_FILE_PATH);
		
		AvatarRule.forFilesAt(sources);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFilesAt_varargVariant_nullValue() {
		AvatarRule.forFilesAt((String) null);
	}
	
	@Test
	public void testForFilesAt_varargVariant_emptyArray() {
		AvatarRule.forFilesAt(new String[]{});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFilesAt_varargVariant_arrayContainingNull() {
		AvatarRule.forFilesAt(new String[]{null});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testForFilesAt_varargVariant_arrayContainingNonExistentFilePath() {
		AvatarRule.forFilesAt(new String[]{"I don't exist"});
	}
	
	@Test
	public void testForFilesAt_varargVariant_arrayContainingOneValidItem() {
		AvatarRule.forFilesAt(new String[]{DATA_FILE_PATH});
	}
	
	@Test
	public void testForFilesAt_varargVariant_iterableContainingMultipleValidItems() {
		AvatarRule.forFilesAt(new String[]{DATA_FILE_PATH, DATA_FILE_PATH});
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetProcessingEnvironment_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule.forFilesAt(DATA_FILE_PATH);
		
		rule.getProcessingEnvironment();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetCompilationResult_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule.forFilesAt(DATA_FILE_PATH);
		
		rule.getCompilationResult();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetRoundEnvironments_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule.forFilesAt(DATA_FILE_PATH);
		
		rule.getRoundEnvironments();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetElementsWithId_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule.forFilesAt(DATA_FILE_PATH);
		
		rule.getElementsWithId("");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetElementsWithUniqueId_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule.forFilesAt(DATA_FILE_PATH);
		
		rule.getElementWithUniqueId("");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetElementsWithAnnotation_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule.forFilesAt(DATA_FILE_PATH);
		
		rule.getElementsWithAnnotation(Annotation.class);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetRootElements_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = AvatarRule.forFilesAt(DATA_FILE_PATH);
		
		rule.getRootElements();
	}
}