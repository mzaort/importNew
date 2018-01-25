package importnew.java8.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import org.junit.Test;

import lombok.Data;

public class AnnotationTest {

	@Test
	public void testValidateInt() throws Exception {
		@Data
		class Cat {
			@ValidateInt(max = 9, min = 1)
			int age;
		}

		Cat cat = new Cat();
		cat.setAge(18);

		Field[] fields = Cat.class.getDeclaredFields();
		if (fields != null) {
			for (Field e : fields) {
				if (e.isAnnotationPresent(ValidateInt.class)) {
					ValidateInt validateInt = e.getAnnotation(ValidateInt.class);
					e.setAccessible(true);
					int age = e.getInt(cat);
					if (age > validateInt.max()) {
						System.out.println("out of max");
					} else if (age < validateInt.min()) {
						System.out.println("out of min");
					} else {
						System.out.println("validated");
					}
				}
			}
		}

		System.out.println(Arrays.toString(Cat.class.getMethods()));
		System.out.println(Arrays.toString(Cat.class.getDeclaredMethods()));
	}

	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface ValidateInt {
		int max();

		int min();
	}

	@Test
	public void testInheritedAnnotation() {
		@Inheritable
		@Filter
		class A {}
		class B extends A {}
		Inheritable annotation = B.class.getAnnotation(Inheritable.class);
		System.out.println(annotation);
		Filter filter = B.class.getAnnotation(Filter.class);
		System.out.println(filter);
	}

	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@interface Inheritable {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
	@interface NonEmpty {}

	public static class Holder<@NonEmpty T> extends @NonEmpty Object {
		public void method() throws @NonEmpty Exception {}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		final Holder<String> holder = new @NonEmpty Holder<String>();
		@NonEmpty
		Collection<@NonEmpty String> strings = new ArrayList<>();
	}

	@Test
	public void testRepeatableAnnotation() {
		Filter[] filters = Filterable.class.getAnnotationsByType(Filter.class);
		Stream.of(filters).forEach(e -> System.out.println(e.value()));
		Filters annotation = Filterable.class.getAnnotation(Filters.class);
		Stream.of(annotation.value()).forEach(e -> System.out.println(e.value()));
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Repeatable(Filters.class)
	@interface Filter {
		String value() default "filter";
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@interface Filters {
		Filter[] value();
	}

	@Filter("filter1")
	@Filter("filter2")
	interface Filterable {

	}
}
