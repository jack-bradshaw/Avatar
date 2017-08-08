package com.matthewtamlin.avatar.rules.avatar_rule.without_running;

import com.google.testing.compile.JavaFileObjects;
import com.matthewtamlin.avatar.rules.AvatarRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.tools.JavaFileObject;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;

@RunWith(JUnit4.class)
public class TestAvatarRule {
	private static final String DATA_FILE_PATH =
			"src/test/java/com/matthewtamlin/avatar/rules/avatar_rule/without_running/Data.java";
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullJavaFileObject() {
		new AvatarRule((JavaFileObject) null);
	}
	
	@Test
	public void testConstructor_validJavaFileObject() throws MalformedURLException {
		final File file = new File(DATA_FILE_PATH);
		final JavaFileObject javaFileObject = JavaFileObjects.forResource(file.toURI().toURL());
		
		new AvatarRule(javaFileObject);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullFile() {
		new AvatarRule((File) null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nonExistentFile() {
		new AvatarRule(new File("I don't exist"));
	}
	
	@Test
	public void testConstructor_validFile() {
		new AvatarRule(new File(DATA_FILE_PATH));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_nullFilePath() {
		new AvatarRule((String) null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_filePathPointsToNonexistentFile() {
		new AvatarRule("I don't exist");
	}
	
	@Test
	public void testConstructor_validFilePath() {
		new AvatarRule(DATA_FILE_PATH);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetProcessingEnvironment_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = new AvatarRule(DATA_FILE_PATH);
		
		rule.getProcessingEnvironment();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetCompilationResult_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = new AvatarRule(DATA_FILE_PATH);
		
		rule.getCompilationResult();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetRoundEnvironments_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = new AvatarRule(DATA_FILE_PATH);
		
		rule.getRoundEnvironments();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetElementsWithId_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = new AvatarRule(DATA_FILE_PATH);
		
		rule.getElementsWithId("");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetElementsWithUniqueId_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = new AvatarRule(DATA_FILE_PATH);
		
		rule.getElementWithUniqueId("");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetElementsWithAnnotation_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = new AvatarRule(DATA_FILE_PATH);
		
		rule.getElementsWithAnnotation(Annotation.class);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetRootElements_failsWhenCalledBeforeRuleIsApplied() {
		final AvatarRule rule = new AvatarRule(DATA_FILE_PATH);
		
		rule.getRootElements();
	}
}