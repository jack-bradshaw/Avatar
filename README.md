# Avatar
Tools for creating Java source code elements in unit tests.

While testing my [Spyglass](https://github.com/MatthewTamlin/Spyglass) library, I was frustrated by how difficult it is to write unit tests for classes which use `javax.tools.Element` objects. There's no easy way to instantiate elements directly, and the usual mocking frameworks produce interconnected networks of unmaintainable mess. Andrew Phillips has done a great job of [articulating the problem](http://blog.xebia.com/testing-annotation-processors/) so I wont go into further detail, other than to say there needs to be a simple way to create elements for unit tests. 

This library solves the problem by providing utilities for directly converting source files to elements. This is the simplest way to create elements, because the developer only has to write normal source code and can largely ignore the complexities of the element API. The elements created by this library behave exactly as elements would in a real scenario (they are after all, real elements) which avoids the need for complex mocking and stubbing.

## Download
Releases are made available through jCentre. Add `compile 'com.matthew-tamlin:avatar:1.0.1'` to your gradle build file to use the latest version.

## Usage
The public API of this library consists of three classes:
- RootElementSupplier
- AnnotatedElementSupplier
- IdBasedElementSupplier

All of the examples in this section require a `JavaFileObject`, but unfortunately the JavaFileObject interface is not trivial to implement and the existing implementations are not always easy to work with. Lucky for us, the Google [compile testing](https://github.com/google/compile-testing) library contains the `JavaFileObjects` utility class which contains many useful methods for getting Java file objects. This utility class is referenced in all of the examples.

### RootElementSupplier
Use the `RootElementSupplier` class to get all root elements. For example, if a source file is defined in `src/main/java/com/matthewtamlin/example/MyClass.java` as:
```java
public class MyClass {
    public void method1() {}
}

class MyOtherClass {
   private static final boolean field1 = true;
   private static final boolean field2 = false;
   public String field3 = "example field";
}
```
then executing the following code:
```java
File srcFile = new File("src/main/java/com/matthewtamlin/example/MyClass.java")
JavaFileObject srcFileObject = JavaFileObjects.forResource(srcFile.toURI().toURL());

RootElementSupplier supplier = new RootElementSupplier(srcFileObject);
Set<Element> foundElements = supplier.getRootElements();

for (Element e : foundElements) {
    System.out.println("Found element " + e.getSimpleName().toString());
}
```
would produce:
```
Found element MyClass
Found element MyOtherClass
```

### AnnotatedElementSupplier
Use the `AnnotatedElementSupplier` class to get all elements with a particular annotation. For example, if a source file is defined in `src/main/java/com/matthewtamlin/example/MyClass.java` as:
```java
@Unobtainium
public class MyClass {
    @Unobtainium
    public void method1() {}
}

@Carbon
class MyOtherClass {
   @Hydrogen
   private static final boolean field1 = true;
   
   @Polonium
   private static final boolean field2 = false;
   
   @Unobtainium   
   public String field3 = "Hello, World!";
}
```
then executing the following code:
```java
File srcFile = new File("src/main/java/com/matthewtamlin/example/MyClass.java")
JavaFileObject srcFileObject = JavaFileObjects.forResource(srcFile.toURI().toURL());

AnnotatedElementSupplier supplier = new AnnotatedElementSupplier(srcFileObject);
Set<Element> foundElements = supplier.getElementsWithAnnotation(Unobtainium.class);

for (Element e : foundElements) {
    System.out.println("Found element " + e.getSimpleName().toString());
}
```
would produce:
```
Found element MyClass
Found element method1
Found element field3
```

### IdBasedElementSupplier
Use the `IdBasedElementSupplier` class to get all elements with a particular ID. Element IDs are defined by adding `ElementId` annotations to the source code. For example, if a source file is defined in `src/main/java/com/matthewtamlin/example/MyClass.java` as:
```java
public class MyClass {
    @ElementId("Cat")
    public String method1(@ElementId("Dog") String parameter1) {
        return parameter;
    }
}

@ElementId("Dog")
class MyOtherClass {
   private static final boolean field1 = true;
   
   @ElementId("Dog")
   private static final boolean field2 = false;
   
   @ElementId("dog")
   public String field3 = "example field";
}
```
then executing the following code:
```java
File srcFile = new File("src/main/java/com/matthewtamlin/example/MyClass.java")
JavaFileObject srcFileObject = JavaFileObjects.forResource(srcFile.toURI().toURL());

IdBasedElementSupplier supplier = new IdBasedElementSupplier(srcFileObject);
Set<Element> foundElements = supplier.getElementsWithId("Dog");

for (Element e : foundElements) {
    System.out.println("Found element " + e.getSimpleName().toString());
}
```
would produce:
```
Found element parameter1
Found element MyOtherClass
Found element field2
```

In addition to the `getElementsWithId` method, the `getUniqueElementWithId(String)` method is provided for convenience. This method returns a single element to avoid the unnecessary overhead of using a set, but it will throw an exception if the supplied ID does not correspond to exactly one element in the source file.

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
