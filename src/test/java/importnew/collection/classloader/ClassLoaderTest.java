package importnew.collection.classloader;

import java.net.URL;

import org.junit.Test;

public class ClassLoaderTest {

	@Test
	public void testClassLoader() {
		System.out.println(ClassLoader.getSystemClassLoader());
		System.out.println(ClassLoader.getSystemClassLoader() == Thread.currentThread().getContextClassLoader());
		System.out.println(String.class.getClassLoader());
		System.out.println(this.getClass().getClassLoader());
		System.out.println(this.getClass().getClassLoader().getParent());
		System.out.println(this.getClass().getClassLoader().getParent().getParent());
		System.out.println(System.getProperty("java.home") + "\\lib\\rt.jar");
	}

	@Test
	public void testGetResource() {
		Class<?> klass = String.class;
		System.out.println(klass.getName().replace('.', '/'));
		URL location = klass.getResource('/' + klass.getName().replace('.', '/') + ".class");
		System.out.println(location);
		location = klass.getResource("/");
		System.out.println(location);

		System.out.println(this.getClass().getResource(""));
		System.out.println(this.getClass().getResource("/"));
		System.out.println(this.getClass().getResource("/java/lang/String.class"));
		System.out.println(this.getClass().getClassLoader().getResource(""));
		System.out.println(this.getClass().getClassLoader().getResource("/")); // null
		System.out.println(this.getClass().getClassLoader().getResource("java/lang/String.class")); // null
	}

	@Test
	public void testProtectedDomain() {
		System.out.println(String.class.getProtectionDomain().getCodeSource());
		System.out.println(this.getClass().getProtectionDomain().getCodeSource());
	}
}
