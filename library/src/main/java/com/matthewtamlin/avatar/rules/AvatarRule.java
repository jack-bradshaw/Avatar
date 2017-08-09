package com.matthewtamlin.avatar.rules;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.testing.compile.JavaFileObjects;
import com.matthewtamlin.avatar.compilation.CompilationResult;
import com.matthewtamlin.avatar.compilation.CompilerUtil;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.*;

import static com.matthewtamlin.avatar.util.IterableNullChecker.checkNotContainsNull;
import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

public class AvatarRule implements TestRule {
	private final Iterable<JavaFileObject> sources;
	
	private final List<RoundEnvironment> roundEnvironments = new ArrayList<>();
	
	private final Set<Element> rootElements = new HashSet<>();
	
	private final Map<String, Set<Element>> elementsByAnnotationName = new HashMap<>();
	
	private final Map<String, Set<Element>> elementsById = new HashMap<>();
	
	private ProcessingEnvironment processingEnvironment;
	
	private CompilationResult compilationResult;
	
	private AvatarRule(final Iterable<JavaFileObject> sources) {
		checkNotNull(sources, "Argument \'sources\' cannot be null.");
		checkNotContainsNull(sources, "Argument \'sources\' cannot contain null.");
		
		this.sources = ImmutableList.copyOf(sources);
	}
	
	public static AvatarRule forJavaFileObjects(final Iterable<JavaFileObject> sources) {
		checkNotNull(sources, "Argument \'sources\' cannot be null.");
		checkNotContainsNull(sources, "Argument \'sources\' cannot contain null.");
		
		return new AvatarRule(sources);
	}
	
	public static AvatarRule forJavaFileObjects(final JavaFileObject... sources) {
		checkNotNull(sources, "Argument \'sources\' cannot be null.");
		
		return AvatarRule.forJavaFileObjects(Arrays.asList(sources));
	}
	
	public static AvatarRule forFiles(final Iterable<File> sources) {
		checkNotNull(sources, "Argument \'sources\' cannot be null.");
		checkNotContainsNull(sources, "Argument \'sources\' cannot contain null.");
		
		final List<JavaFileObject> javaFileObjects = new ArrayList<>();
		
		for (final File source : sources) {
			if (!source.exists()) {
				throw new IllegalArgumentException("File \'" + source + "\' does not exist.");
			}
			
			try {
				javaFileObjects.add(JavaFileObjects.forResource(source.toURI().toURL()));
			} catch (final MalformedURLException e) {
				throw new RuntimeException("Could not get URL for file \'" + source + "\'.", e);
			}
		}
		
		return AvatarRule.forJavaFileObjects(javaFileObjects);
	}
	
	public static AvatarRule forFiles(final File... sources) {
		checkNotNull(sources, "Argument \'sources\' cannot be null.");
		
		return AvatarRule.forFiles(Arrays.asList(sources));
	}
	
	public static AvatarRule forFilesAt(final Iterable<String> sourcePaths) {
		checkNotNull(sourcePaths, "Argument \'sourcePaths\' cannot be null.");
		checkNotContainsNull(sourcePaths, "Argument \'sourcePaths\' cannot contain null.");
		
		final List<File> files = new ArrayList<>();
		
		for (final String sourcePath : sourcePaths) {
			final File sourceFile = new File(sourcePath);
			
			if (!sourceFile.exists()) {
				throw new IllegalArgumentException("File \'" + sourceFile + "\' does not exist.");
			}
			
			files.add(sourceFile);
		}
		
		return AvatarRule.forFiles(files);
	}
	
	public static AvatarRule forFilesAt(final String... sourcePaths) {
		checkNotNull(sourcePaths, "Argument \'sourcePaths\' cannot be null.");
		
		return AvatarRule.forFilesAt(Arrays.asList(sourcePaths));
	}
	
	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				compilationResult = CompilerUtil.compileUsingProcessor(new AvatarRule.Processor(), sources);
				
				base.evaluate();
			}
		};
	}
	
	public ProcessingEnvironment getProcessingEnvironment() {
		if (compilationResult == null) {
			throw new IllegalStateException("Rule must be evaluated before accessing processing environment.");
		}
		
		return processingEnvironment;
	}
	
	public CompilationResult getCompilationResult() {
		if (compilationResult == null) {
			throw new IllegalStateException("Rule must be evaluated before accessing compilation result.");
		}
		
		return compilationResult;
	}
	
	public List<RoundEnvironment> getRoundEnvironments() {
		if (compilationResult == null) {
			throw new IllegalStateException("Rule must be evaluated before accessing round environments.");
		}
		
		return roundEnvironments;
	}
	
	public Set<Element> getElementsWithId(final String id) {
		checkNotNull(id, "Argument \'id\' cannot be null.");
		
		if (compilationResult == null) {
			throw new IllegalStateException("Rule must be evaluated before accessing elements.");
		}
		
		if (elementsById.containsKey(id)) {
			return elementsById.get(id);
		} else {
			return new HashSet<>();
		}
	}
	
	public Element getElementWithUniqueId(final String id) {
		checkNotNull(id, "Argument \'id\' cannot be null.");
		
		if (compilationResult == null) {
			throw new IllegalStateException("Rule must be evaluated before accessing elements.");
		}
		
		if (getElementsWithId(id).isEmpty()) {
			throw new UniqueElementNotFoundException("No elements found for ID \'" + id + "\'.");
		}
		
		if (getElementsWithId(id).size() > 1) {
			throw new UniqueElementNotFoundException("Multiple elements found for ID \'" + id + "\'.");
		}
		
		return getElementsWithId(id).iterator().next();
	}
	
	public Set<Element> getElementsWithAnnotation(Class<? extends Annotation> annotationClass) {
		checkNotNull(annotationClass, "Argument \'annotationClass\' cannot be null.");
		
		if (compilationResult == null) {
			throw new IllegalStateException("Rule must be evaluated before accessing elements.");
		}
		
		if (elementsByAnnotationName.containsKey(annotationClass.getCanonicalName())) {
			return elementsByAnnotationName.get(annotationClass.getCanonicalName());
		} else {
			return new HashSet<>();
		}
	}
	
	public Set<Element> getRootElements() {
		if (compilationResult == null) {
			throw new IllegalStateException("Rule must be evaluated before accessing elements.");
		}
		
		return rootElements;
	}
	
	private class Processor extends AbstractProcessor {
		@Override
		public synchronized void init(final ProcessingEnvironment processingEnvironment) {
			super.init(processingEnvironment);
			AvatarRule.this.processingEnvironment = processingEnvironment;
		}
		
		@Override
		public Set<String> getSupportedAnnotationTypes() {
			return ImmutableSet.of("*");
		}
		
		@Override
		public boolean process(
				final Set<? extends TypeElement> annotations,
				final RoundEnvironment roundEnvironment) {
			
			roundEnvironments.add(roundEnvironment);
			rootElements.addAll(roundEnvironment.getRootElements());
			
			collectElementsByAnnotation(annotations, roundEnvironment);
			collectElementsById(roundEnvironment);
			
			return false;
		}
		
		private void collectElementsByAnnotation(
				final Set<? extends TypeElement> annotations,
				final RoundEnvironment roundEnvironment) {
			
			for (final TypeElement annotation : annotations) {
				final String annotationName = annotation.getQualifiedName().toString();
				
				if (!elementsByAnnotationName.containsKey(annotationName)) {
					elementsByAnnotationName.put(annotationName, new HashSet<Element>());
				}
				
				elementsByAnnotationName
						.get(annotationName)
						.addAll(roundEnvironment.getElementsAnnotatedWith(annotation));
			}
		}
		
		private void collectElementsById(final RoundEnvironment roundEnvironment) {
			for (final Element e : roundEnvironment.getElementsAnnotatedWith(ElementId.class)) {
				final ElementId elementId = e.getAnnotation(ElementId.class);
				final String id = elementId.value();
				
				if (!elementsById.containsKey(id)) {
					elementsById.put(id, new HashSet<Element>());
				}
				
				elementsById.get(id).add(e);
			}
		}
	}
}