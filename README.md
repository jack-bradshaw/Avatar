# Avatar
Tools for testing Java annotation processors.
		
Writing automated tests for annotation processors is a difficult task. Many important classes in the annotation processing API cannot be directly instantiated, and the complexity of the API renders the standard mocking frameworks ineffective. This library contains a custom JUnit test rule which solves these issues by:
- Providing `javax.model.Element` instances.
- Providing access to the annotation processor utilities.
- Providing access to other compile-time resources data.

## Dependency
Releases are published to [JCenter](https://bintray.com/bintray/jcenter). To use the latest release:

Gradle:
```
compile 'com.matthew-tamlin:avatar:1.0.1'
```

Maven: 
```
<dependency>
  <groupId>com.matthew-tamlin</groupId>
  <artifactId>avatar</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

Older versions are available in [the Maven repo](https://bintray.com/matthewtamlin/maven/Avatar).

## Tutorial
This tutorial covers the following:
- Getting started.
- Using Avatar to get element instances in tests.
- Using Avatar to get annotation processing utilities in tests.
- Using Avatar to get other compile-time resources in tests.
		
### Getting started		
Start by instantiating the `AvatarRule` in the test class. The builder pattern provides methods for defining the sources to compile and setting whether or not compilation must succeed. Some examples:
		
```java
public class Tests {
	// Compiles one source file, referenced by path.
	@Rule
	public AvatarRule rule1 = AvatarRule
			.builder()
			.withSourcesAt("src/com/example/Source.java")
			.build();
	
	// Compiles multiple source files, both referenced by path.
	@Rule
	public AvatarRule rule2 = AvatarRule
			.builder()
			.withSourcesAt(
				"src/com/example/Source1.java", 
				"src/com/example/Source2.java")
			.build();
	
	// Compiles multiple source files, one referenced by path and one referenced by file.
	@Rule
	public AvatarRule rule3 = AvatarRule
			.builder()
			.withSources(new File("some_path/File.java"))
			.withSourcesAt("some_other_path/File.java")
			.build();
	
	// Compilation will fail, causing the the rule to fail before the tests run.
	@Rule
	public AvatarRule rule4 = AvatarRule
			.builder()
			.withSourcesAt("src/com/DoesntCompile.java")
			.withSuccessfulCompilationRequired(true)
			.build();

	// Compilation will fail, but the rule will still pass and the tests will run.
	@Rule
	public AvatarRule rule5 = AvatarRule
			.builder()
			.withSourcesAt("src/com/DoesntCompile.java")
			.withSuccessfulCompilationRequired(false)
			.build();
}
```

### Getting elements
When the avatar rule runs, it collects elements from the compiled sources so that they can be used in the tests.

Consider a source file at `src/test/com/example` containing:
```java
package com.example;

@ElementId("class")
public class TestData {
	@ElementId("method")
	public void someMethod1(@ElementId("param") int val1, @ElementId("param") int val2) {}
	
	@SomeAnnotation
	public void someMethod2() {}
	
	@SomeAnnotation
	public Object someField1 = null;
	
	@SomeAnnotation
	public String someField2 = "";
}
```

The avatar rule can be used to get all root elements from the source file, for example:
```java
@RunWith(JUnit4.class)
public void TestSomething {
	@Rule
	public final AvatarRule rule = AvatarRule
			.builder()
			.withSourcesAt("src/test/com/example/TestData.java")
			.build();
			
	@Test
	public void test() {
		// Contains the 'TestData' type element
		Set<Element> elements = rule.getRootElements();
	}
}
```

The avatar rule can be used to get elements from the source file based on the IDs defined by `@ElementId`, for example:
```java
@RunWith(JUnit4.class)
public void TestSomething {
	@Rule
	public final AvatarRule rule = AvatarRule
			.builder()
			.withSourcesAt("src/test/com/example/TestData.java")
			.build();
			
	@Test
	public void test() {
		// The 'TestData' type element
		Element classElement = rule.getUniqueElementWithId("class");
		
		// The 'someMethod' executable element 
		Element methodElement = rule.getUniqueElementWithId("method");
		
		// Contains the 'val1' and 'val2' variable element
		Set<Element> parameterElements = rule.getElementsWithId("param");
	}
}
```

The avatar rule can be used to get elements from the source file based on the annotations, for example:
```java
@RunWith(JUnit4.class)
public void TestSomething {
	@Rule
	public final AvatarRule rule = AvatarRule
			.builder()
			.withSourcesAt("src/test/com/example/TestData.java")
			.build();
			
	@Test
	public void test() {
		/* 
		 * Contains the 'someMethod2' executable element, as well as the 'someField1' and 
		 * 'someField2' variable elements.
		 */
		Set<Element> elements = rule.getElementsWithAnnotation(SomeAnnotation.class);
	}
}
```

### Getting annotation processing utilities
The avatar rule provides access to the utilities used during annotation processing, such as the `Elements` class and the `Types` class. For example:
```java
public void TestSomething {
	@Rule
	public final AvatarRule rule = AvatarRule.withoutSources();
	
	private Elements elementUtil;
	
	private Types typeUtil;
	
	private Filer filer;
	
	private Messager messager;
	
	@Before
	public void setupUtils() {
		final ProcessingEnvironment environment = rule.getProcessingEnvironment();
		
		elementUtil = environment.getElementUtils();
		typeUtil = environment.getTypeUtils();
		filer = environment.getFiler();
		messager = environment.getMessager();
	}
}
```

The returned objects are fully functional and can be used to unit test dependent classes.

### Getting other compile-time resources
The avatar rule provides access to other compile-time resources. For example:
```java
public void TestSomething {
	// Using the Google compile testing class to parse a source string
	@Rule
	public final AvatarRule rule = AvatarRule
			.builder()
			.withSourcesAt("some_path/BadJavaFile.java")
			.withSuccessfulCompilationRequired(false)e
			.build();
	
	private CompilationResult compilationResult;
	
	private List<RoundEnvironment> roundEnvironments;
	
	@Before
	public void setupCompilerResources() {
		// The compilation outcome (fail or pass), the generated files and any diagnostics
		compilationResult = rule.getCompilationResult();
		
		// The round environments provided by the Java compiler during compilation
		roundEnvironments = rule.getRoundEnvironments();
	}
}
```

## Licensing
This library is licenced under the Apache v2.0 licence. Have a look at [the license](LICENSE) for details.

## Dependencies and Attribution
This library uses the following open source libraries as level 1 dependencies:
- [AutoValue](https://github.com/google/auto/tree/master/value), licensed under the Apache 2.0 licence.
- [CompileTesting](https://github.com/google/compile-testing), licensed under the Apache 2.0 licence.
- [Guava](https://github.com/google/guava), licensed under the Apache 2.0 licence.
- [Java Utilities](https://github.com/MatthewTamlin/JavaUtilities), licensed under the Apache 2.0 licence.
- [JUnit](http://junit.org/junit4/), licensed under the Eclipse Public License 1.0.

## Compatibility
This library is compatible with Java 1.7 and up.
