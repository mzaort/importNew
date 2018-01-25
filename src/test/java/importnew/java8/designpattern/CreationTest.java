package importnew.java8.designpattern;

import org.junit.Test;

public class CreationTest {

	@Test
	public void testSingleton() {
		SingletonA instance = SingletonA.instance();
		System.out.println(instance);
		System.out.println(instance == SingletonA.instance());
	}

	@Test
	public void testVariableParameters() {
		InterfaceB.acceptAll("a", "b", "C");
		InterfaceB.acceptAll("a", 1, 2);
		InterfaceB.acceptAll(1, 2);
	}

}

class SingletonA {
	private SingletonA() {}

	public static SingletonA instance() {
		return InnerB.a;
	}

	static class InnerB {
		static SingletonA a = new SingletonA();
	}
}

interface InterfaceB {
	@SafeVarargs
	static <T> void acceptAll(T... all) {
		System.out.println(all);
	}

	@SafeVarargs
	static <T> void acceptAll(String message, T... all) {
		System.out.println(message);
		System.out.println(all);
	}
}
