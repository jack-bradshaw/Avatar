package com.matthewtamlin.avatar.rules.avatar_rule.with_running;

import com.matthewtamlin.avatar.compilation.CompilationResult;
import com.matthewtamlin.avatar.rules.AvatarRule;
import org.junit.Rule;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestAvatarRuleWithNoCode {
	@Rule
	public final AvatarRule rule1 = AvatarRule.withoutSources();
	
	@Rule
	public final AvatarRule rule2 = AvatarRule.builder().build();
	
	@Test
	public void testGetProcessingEnvironment() {
		final ProcessingEnvironment processingEnvironment1 = rule1.getProcessingEnvironment();
		final ProcessingEnvironment processingEnvironment2 = rule2.getProcessingEnvironment();
		
		assertThat(processingEnvironment1, is(notNullValue()));
		assertThat(processingEnvironment2, is(notNullValue()));
	}
	
	@Test
	public void testGetCompilationResult() {
		final CompilationResult compilationResult1 = rule1.getCompilationResult();
		final CompilationResult compilationResult2 = rule2.getCompilationResult();
		
		assertThat(compilationResult1, is(notNullValue()));
		assertThat(compilationResult2, is(notNullValue()));
		
		assertThat(compilationResult1.success(), is(true));
		assertThat(compilationResult2.success(), is(true));
	}
	
	@Test
	public void testGetRoundEnvironments() {
		final Collection<RoundEnvironment> roundEnvironments1 = rule1.getRoundEnvironments();
		final Collection<RoundEnvironment> roundEnvironments2 = rule2.getRoundEnvironments();
		
		assertThat(roundEnvironments1, is(notNullValue()));
		assertThat(roundEnvironments2, is(notNullValue()));
	}
}