package importnew.java8.collection;

import org.junit.Test;

public class GrammarTest {

	@Test
	public void testFinally() {
		class Test {
			String fun() {
				String x = "This is a string";
				try {
					return x.length() + "A";
				} finally {
					x = null;
				}
			}
		}

		Test test = new Test();
		System.out.println(test.fun());
	}

}
