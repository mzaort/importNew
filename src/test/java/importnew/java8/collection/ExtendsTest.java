package importnew.java8.collection;

import org.junit.Test;

import lombok.Data;

public class ExtendsTest {

	@Test
	public void testSuper() {
		@Data
		class A {
			int age;

			@Override
			public String toString() {
				return "hello" + A.this.age;
			}

			public String toString2() {
				String str = "world";
				str += A.super.toString();
				return str;
			}
		}

		A a = new A();
		System.out.println(a);
		System.out.println(a.toString2());
	}

}
