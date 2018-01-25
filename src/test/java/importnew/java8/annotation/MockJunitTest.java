package importnew.java8.annotation;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.Test;

public class MockJunitTest {

	@Test
	public void testMockJunitWithSpecificException() throws Exception {
		int passed = 0;
		int failed = 0;
		Class<?> mock = Class.forName("importnew.java8.annotation.MockJunitTest");
		Object obj = mock.newInstance();
		for (Method method : mock.getMethods()) {
			if (method.isAnnotationPresent(TestableException.class)) {
				try {
					method.invoke(obj);
					++failed;
				} catch (IllegalAccessException | InvocationTargetException e) {
					Throwable cause = e.getCause();
					TestableException exception = method.getAnnotation(TestableException.class);
					if (cause != null && exception != null && exception.value() != null
							&& Stream.of(exception.value()).anyMatch(clazz -> clazz.isInstance(cause))) {
						++passed;
					} else {
						System.out.printf("Method %s test failed: %s", method.getName(), e.getCause());
						e.printStackTrace();
						++failed;
					}
				}
			}
		}
		System.out.printf("Success/Total: [%d/%d]\n", passed, passed + failed);
	}

	@TestableException(IOException.class)
	public void testIOException() throws IOException {
		System.out.println("IO Exception");
		throw new IOException("IO Exception");
	}

	@TestableException(RuntimeException.class)
	public void testRunTimeException() throws IOException {
		System.out.println("Runtime Exception");
		throw new IOException("Runtime Exception");
	}

	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface TestableException {
		Class<? extends Throwable>[] value();
	}

	@Test
	public void testMockJunit() throws Exception {
		int passed = 0;
		int failed = 0;
		Class<?> mock = Class.forName("importnew.java8.annotation.MockJunitTest");
		Object obj = mock.newInstance();
		for (Method method : mock.getMethods()) {
			if (method.isAnnotationPresent(Testable.class)) {
				try {
					method.invoke(obj);
					++passed;
				} catch (IllegalAccessException | InvocationTargetException e) {
					System.out.printf("Method %s test failed: %s", method.getName(), e.getCause());
					e.printStackTrace(System.out);
					++failed;
				}
			}
		}
		System.out.printf("Success/Total: [%d/%d]\n", passed, passed + failed);
	}

	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Testable {}

	@Testable
	public void test1() {
		System.out.println("test1");
	}

	public void test2() throws IOException {
		System.out.println("test2");
		throw new IOException("IO Exception");
	}

	@Testable
	public void test3() {
		System.out.println("test3");
		throw new RuntimeException("Runtime Exception");
	}

	public void test4() {
		System.out.println("test4");
	}

	@Testable
	public void test5() {
		System.out.println("test5");
	}

	@Testable
	public void test6() {
		System.out.println("test6");
	}

}
