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
		final Set<Element> elements = rule.getRootElements();
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
		final Element classElement = rule.getUniqueElementWithId("class");
		
		// The 'someMethod' executable element 
		final Element methodElement = rule.getUniqueElementWithId("method");
		
		// Contains the 'val1' and 'val2' variable element
		final Set<Element> parameterElements = rule.getElementsWithId("param");
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
		final Set<Element> elements = rule.getElementsWithAnnotation(SomeAnnotation.class);
	}
}
```

## Realistic scenario
To demonstrate the usefulness of this library, this section contains an example which walks through a realistic scenario where this library is useful. The scenario involves creating and testing two components of an annotation processor project:
- An annotation which can be used to mark methods that return void
- A validator which checks if the annotation has been correctly applied to the source code

First we will define a few source files, and then we will write some unit tests using this library. I'm going to assume you're familiar with the basic concepts of Java annotations, annotation processors and unit testing.

### Source files
Consider an annotation which we can use to mark methods that return void. This doesn't seem to have much practical benefit, but it's useful for the example. The annotation is defined in `src/main/java/com/matthewtamlin/example/ReturnsNothing.java` as:
```java
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface ReturnsNothing {}
```

If we wish to use the annotation in an annotation processor, we probably want to write a validator to make sure the annotation has been correctly applied to the source code. The validator is defined in `src/main/java/com/matthewtamlin/example/Validator.java` as:
```java
public class Validator {
    public static void validate(Element element) throws ValidationException {
        if (element == null) {
            return;
        }
        
        if (!(element instanceof ExecutableElement)) {
            return;
        }
        
        if (element.getAnnotation(ReturnsNothing.class) == null) {
            return;
        }
        
        TypeMirror returnType = ((ExecutableElement) element).getReturnType();
        
        if (returnType instanceof NoType && ((NoType) returnType).equals(TypeKind.VOID) {
            // This is fine
        } else {
            throw new ValidationException("ReturnsNothing tag found with non-void return type");
        }
    }
}
```

Great, now we have some source files.

### Test files
We're good developers (although sadly, not test driven developers) so we decide we want to write some unit tests to make sure the validation logic is working as expected. We want to make sure the validator obeys the following rules:
- Validation passes if the element is null
- Validation passes if the element is not an ExecutableElement
- Validation passes if the element is missing the annotation and returns void
- Validation passes if the element is missing the annotation and returns a primitive
- Validation passes if the element is missing the annotation and returns an object
- Validation passes if the element has the annotation and returns void
- Validation fails if the element has the annotation and returns a primitive
- Validation fails if the element has the annotation and returns an object

To test these rules, we need to pass the validator specific elements and compare the actual results to the expected outcomes. We can define the elements by creating a data class and using this library to convert it to elements. The data class is defined in `src/test/java/com/matthewtamlin/example/TestValidatorData.java` as:
```java
public class TestValidatorData {
    @ElementId("void with annotation")
    @ReturnsNothing
    public void method1() {}
    
    @ElementId("void without annotation")
    public void method2() {}
    
    @ElementId("primitive with annotation")
    @ReturnsNothing
    public int method3() {}
    
    @ElementId("primitive without annotation")
    public int method4() {}
    
    @ElementId("object with annotation")
    @ReturnsNothing
    public Object method5() {}
    
    @ElementId("object without annotation")
    public Object method6() {}
}
```

Now that we have the data class, we can use it to write the unit tests. The unit tests are defined in `src/test/java/com/matthewtamlin/example/TestValidator.java` as:
```java
@RunWith(JUnit4.class)
public class TestValidator {
    private static final File SRC_FILE = new File("src/test/java/com/matthewtamlin/example" +
            "/TestValidatorData.java");
    
    private IdBasedElementSupplier supplier;
    
    @Before
    public void setUp() {
        assertThat("The source file cannot be found.", SRC_FILE.exists(), is(true));
        
        // Using the Google compile-testing library again
        srcFileObject = JavaFileObjects.forResource(SRC_FILE.toURI().toURL());
        
        supplier = new IdBasedElementSupplier(srcFileObject);
    }
    
    @Test
    public void testValidate_nullElement() {
        Validator.validate(null);
    }
    
    @Test
    public void testValidate_noAnnotationVoidReturn() {
        Element e = supplier.getUniqueElementForId("void without annotation");
        Validator.validate(e);
    }
    
    @Test
    public void testValidate_noAnnotationPrimitiveReturn() {
        Element e = supplier.getUniqueElementForId("primitive without annotation");
        Validator.validate(e);
    }
    
    @Test
    public void testValidate_noAnnotationObjectReturn() {
        Element e = supplier.getUniqueElementForId("object without annotation");
        Validator.validate(e);
    }
    
    @Test
    public void testValidate_annotationPresentVoidReturn() {
        Element e = supplier.getUniqueElementForId("void with annotation");
        Validator.validate(e);
    }
    
    @Test(expected = ValidationException.class)
    public void testValidate_annotationPresentPrimitiveReturn() {
        Element e = supplier.getUniqueElementForId("primitive with annotation");
        Validator.validate(e);
    }
    
    @Test(expected = ValidationException.class)
    public void testValidate_annotationPresentObjectReturn() {
        Element e = supplier.getUniqueElementForId("object with annotation");
        Validator.validate(e);
    }
}
```

Viola! We now have some units tests which can verify the behaviour of the validator.

## Licensing
This library is licenced under the Apache v2.0 licence. Have a look at [the license](LICENSE) for details.

## Dependencies and Attribution
This library uses the following open source libraries as level 1 dependencies:
- [Guava](https://github.com/google/guava), licensed under the Apache 2.0 license.
- [Java Utilities](https://github.com/MatthewTamlin/JavaUtilities), licensed under the Apache 2.0 license.

## Compatibility
This library is compatible with Java 1.7 and up.
