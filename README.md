# JavaCompilerUtilities
While testing my [Spyglass](https://github.com/MatthewTamlin/Spyglass) library, I was frustrated by how difficult it is to write unit tests for classes which use `javax.tools.Element` objects. There's no way to easily instantiate elements directly, and the usual mocking frameworks produce interconnected networks of unmaintainable mess. Andrew Phillips has done a great job of articulating the problem in his [blog post](http://blog.xebia.com/testing-annotation-processors/) so I wont go into further detail, other than to say there needs to be an easy way to create elements for unit tests. 

This library solves the problem by providing a utility which directly converts source files to elements. This is the simplest way to create elements, because the developer only has to write normal source code and can largely ignore the complexities of the element API. The elements created by this library behave exactly as elements would in a real scenario (they are after all, real elements) which avoids the need for complex mocking and stubbing.

## Download
Releases are made available through jCentre. Add `compile 'com.matthew-tamlin:java-compiler-utilities:1.0.0'` to your gradle build file to use the latest version.

## Basic examples
These examples provide just enough information to start using the library. The following classes are covered:
- RootElementSupplier
- AnnotatedElementSupplier
- IdBasedElementSupplier

In all of the examples a JavaFileObject is needed, but unfortunately the JavaFileObject interface is not trivial to implement and the existing implementations are not always easy to work with. Lucky for us, the Google [compile testing](https://github.com/google/compile-testing) library contains the `JavaFileObjects` utility class which contains many useful methods for getting Java file objects. This utility class is referenced in all of the examples.

### RootElementSupplier
Use the RootElementSupplier class to get all root elements from a source file. For example, if a source file is defined in `src/main/java/com/matthewtamlin/example/MyClass.java` as:
```java
public class MyClass {
    @SomeAnnotation
    public String method1() {
        return "example method";
    }
}

@SomeAnnotation
class MyOtherClass {
   private static final boolean field1 = true;
   
   @SomeAnnotation
   private static final boolean field2 = false;
   
   @ElementId("some other ID")
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
produces:
```
Found element MyClass
Found element MyOtherClass
```
### AnnotatedElementSupplier
Use the AnnotatedElementSupplier class to get all elements in a file which have a particular annotation. For example, if a source file is defined in `src/main/java/com/matthewtamlin/example/MyClass.java` as:
```java
@Unobtainium
public class MyClass {
    @Unobtainium
    public void method1() {}
}

class MyOtherClass {
   private static final boolean field1 = true;
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
produces:
```
Found element MyClass
Found element method1
Found element field3
```

### IdBasedElementSupplier
Use the IdBasedElementSupplier class to get all elements in a file which have a particular ID. Element IDs are defined by adding `ElementId` annotations to the source code. For example, if the source is defined in `src/main/java/com/matthewtamlin/example/MyClass.java` as:
```java
public class MyClass {
    @ElementId("Cat")
    public String method1() {
        return "example method";
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
produces:
```
Found element field1
Found element field2
```

The `getUniqueElementWithId(String)` method is also provided for convenience. This method returns a single element to avoid the unnecessary overhead of using a set, but it will throw an exception if the supplied ID does not correspond to exactly one element in the source file.

## End-to-end example
Some context is necessary for a good example, so we will define a few source files and then some tests files. This example assumes you are familiar with the basic concepts of Java annotations, annotation processors and unit testing.

### Source files
Imagine we have an annotation which can be used to mark methods which return void. If the annotation is used in an annotation processor, we probably want to write a validator to make sure the annotation has actually been applied correctly.

The annotation is defined in `src/main/java/com/matthewtamlin/example/ReturnsNothing.java` as:
```java
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface ReturnsNothing {}
```

The validator is defined in `src/main/java/com/matthewtamlin/example/Validator.java` as:
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
### Test files
We're good developers, so we decide we want to write some unit tests to make sure the validation logic is working as expected. We want to make sure the validator obeys the following rules:
- Validation passes if the element is null
- Validation passes if the element is not an ExecutableElement
- Validation passes if the element is missing the annotation and returns void
- Validation passes if the element is missing the annotation and returns a primitive
- Validation passes if the element is missing the annotation and returns an object
- Validation passes if the element has the annotation and returns void
- Validation fails if the element has the annotation and returns a primitive
- Validation fails if the element has the annotation and returns an object

To test the validator, we need to pass it specific elements and compare the actual results to the expected outcomes. Getting mock elements which actually behave as real elements is hard, but luckily this library provides the ElementUtil class. This utility allows us to directly convert a source file to an element model, so that we can use real elements in our unit test. 

We define the source file we wish to convert in `src/test/java/com/matthewtamlin/example/TestValidatorData.java` as:
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

The `@ElementId` annotations provide unique identifiers for the elements, so that the `ElementUtil.getUniqueElementById()` method can be used to get references to them. Using the utility class we define our test class in `src/test/java/com/matthewtamlin/example/TestValidator.java` as:
```java
@RunWith(JUnit4.class)
public class TestValidator {
    private static final File SRC_FILE = new File("src/test/java/com/matthewtamlin/example" +
            "/TestValidatorData.java");
    
    private JavaFileObject srcFileObject;
    
    @Before
    public void setUp() {
        assertThat("The source file cannot be found.", SRC_FILE.exists(), is(true));
        
        // The Google compile-testing library contains a great utility for creating JavaFileObjects
        srcFileObject = JavaFileObjects.forResource(SRC_FILE.toURI().toURL());
    }
    
    @Test
    public void testValidate_nullElement() {
        Validator.validate(null);
    }
    
    @Test
    public void testValidate_noAnnotationVoidReturn() {
        Element e = ElementUtil.getUniqueElementById(
                srcFileObject, 
                "void without annotation");
                
        Validator.validate(e);
    }
    
    @Test
    public void testValidate_noAnnotationPrimitiveReturn() {
        Element e = ElementUtil.getUniqueElementById(
                srcFileObject, 
                "primitive without annotation");
                
        Validator.validate(e);
    }
    
    @Test
    public void testValidate_noAnnotationObjectReturn() {
        Element e = ElementUtil.getUniqueElementById(
                srcFileObject, 
                "object without annotation");
                
        Validator.validate(e);
    }
    
    @Test
    public void testValidate_annotationPresentVoidReturn() {
        Element e = ElementUtil.getUniqueElementById(
                srcFileObject, 
                "void with annotation");
                
        Validator.validate(e);
    }
    
    @Test(expected = ValidationException.class)
    public void testValidate_annotationPresentPrimitiveReturn() {
        Element e = ElementUtil.getUniqueElementById(
                srcFileObject, 
                "primitive with annotation");
                
        Validator.validate(e);
    }
    
    @Test(expected = ValidationException.class)
    public void testValidate_annotationPresentObjectReturn() {
        Element e = ElementUtil.getUniqueElementById(
                srcFileObject, 
                "object with annotation");
                
        Validator.validate(e);
    }
}
```

This produces a standard test class which can be run against the JVM. And thus concludes our in depth example.
