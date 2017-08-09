package com.matthewtamlin.avatar.compilation.compiler_util;

import com.google.testing.compile.JavaFileObjects;
import com.matthewtamlin.avatar.compilation.CompilationResult;
import com.matthewtamlin.avatar.compilation.CompilerUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class TestCompilerUtil {
	private static final File DATA_FILE_1 = new File(
			"src/test/java/com/matthewtamlin/avatar/compilation/compiler_util/Data1.java");
	
	private static final File DATA_FILE_2 = new File(
			"src/test/java/com/matthewtamlin/avatar/compilation/compiler_util/Data2.java");
	
	private JavaFileObject javaFileObject1;
	
	private JavaFileObject javaFileObject2;
	
	@Before
	public void setup() throws MalformedURLException {
		javaFileObject1 = JavaFileObjects.forResource(DATA_FILE_1.toURI().toURL());
		javaFileObject2 = JavaFileObjects.forResource(DATA_FILE_2.toURI().toURL());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCompileUsingProcessor_iterableVariant_nullProcessor() {
		CompilerUtil.compileUsingProcessor(null, mock(JavaFileObject.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCompileUsingProcessor_iterableVariant_nullSources() {
		CompilerUtil.compileUsingProcessor(mock(Processor.class), (Iterable<JavaFileObject>) null);
	}
	
	@Test
	public void testCompileUsingProcessor_iterableVariant_emptySources() {
		CompilerUtil.compileUsingProcessor(mock(Processor.class), new ArrayList<JavaFileObject>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCompileUsingProcessor_iterableVariant_sourcesContainingNull() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(null);
		
		CompilerUtil.compileUsingProcessor(mock(Processor.class), sources);
	}
	
	@Test
	public void testCompileUsingProcessor_iterableVariant_sourcesContainingOneValidItem() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(javaFileObject1);
		
		final CompilationResult result = CompilerUtil.compileUsingProcessor(new MockProcessor(), sources);
		
		assertThat(result.success(), is(true));
	}
	
	@Test
	public void testCompileUsingProcessor_iterableVariant_sourcesContainingMultipleValidItems() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(javaFileObject1);
		sources.add(javaFileObject2);
		
		final CompilationResult result = CompilerUtil.compileUsingProcessor(new MockProcessor(), sources);
		
		assertThat(result.success(), is(true));
	}
	
	@Test
	public void testCompileUsingProcessor_iterableVariant_sourcesContainingOneElementWhichFailsCompilation() {
		final List<JavaFileObject> sources = new ArrayList<>();
		sources.add(JavaFileObjects.forSourceLines("Test", "public abstract final static volatile class Test {}"));
		
		final CompilationResult result = CompilerUtil.compileUsingProcessor(new MockProcessor(), sources);
		
		assertThat(result.success(), is(false));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCompileUsingProcessor_varargsVariant_nullProcessor() {
		CompilerUtil.compileUsingProcessor(null, mock(JavaFileObject.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCompileUsingProcessor_varargsVariant_nullSources() {
		CompilerUtil.compileUsingProcessor(mock(Processor.class), (JavaFileObject) null);
	}
	
	@SuppressWarnings("RedundantArrayCreation")
	@Test
	public void testCompileUsingProcessor_varargsVariant_emptySources() {
		CompilerUtil.compileUsingProcessor(mock(Processor.class), new JavaFileObject[] {});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCompileUsingProcessor_varargsVariant_sourcesContainingNull() {
		CompilerUtil.compileUsingProcessor(mock(Processor.class), new JavaFileObject[] {null});
	}
	
	@Test
	public void testCompileUsingProcessor_varargsVariant_sourcesContainingOneValidItem() {
		final CompilationResult result = CompilerUtil.compileUsingProcessor(new MockProcessor(), javaFileObject1);
		
		assertThat(result.success(), is(true));
	}
	
	@Test
	public void testCompileUsingProcessor_varargsVariant_sourcesContainingMultipleValidItems() {
		final CompilationResult result = CompilerUtil.compileUsingProcessor(
				new MockProcessor(),
				javaFileObject1,
				javaFileObject2);
		
		assertThat(result.success(), is(true));
	}
	
	@Test
	public void testCompileUsingProcessor_varargsVariant_sourcesContainingOneElementWhichFailsCompilation() {
		final CompilationResult result = CompilerUtil.compileUsingProcessor(
				new MockProcessor(),
				JavaFileObjects.forSourceLines("Test", "public abstract final static volatile class Test {}"));
		
		assertThat(result.success(), is(false));
	}
	
	private static class MockProcessor extends AbstractProcessor {
		@Override
		public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
			return false;
		}
	}
}