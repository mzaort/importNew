package importnew.java8.functional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;

public class LambdaTest {
	
	@Test
	public void testIntFunction() {
		IntFunction<String[]> x = String[]::new;
		String[] d = x.apply(10);
		System.out.println(d.length);
	}

	@Test
	public void testGenerateStream1() {
		// filter distinct skip limit map flatMap sorted
		// anyMatch noneMatch allMatch findAny findFirst
		// forEach count min max
		// reduce collect toArray

		// peek(System.out::println)

		// of empty builder generate

		Stream.of("one", "two", "three", "four").forEachOrdered(System.out::println);
		String[] strArr = Stream.of("one", "two", "three", "four").toArray(String[]::new);
		System.out.println(Arrays.asList(strArr));

		Stream.generate(Math::random).limit(10).forEach(System.out::println);
		Stream.iterate(12, e -> e + 1).limit(10).forEach(System.out::println);
	}

	@Test
	public void testMethodReference() {
		class MethodReferece {

			public void consumer(Consumer<String> consumer) {
				consumer.accept("123");
			}

			public Integer function(Function<String, Integer> function) {
				return function.apply("123");
			}

		}

		MethodReferece mf = new MethodReferece();
		mf.consumer(Integer::parseInt);
		System.out.println(mf.function(Integer::parseInt));

		Function<Integer, int[]> newArr = int[]::new;
		int[] arr = newArr.apply(10);
		System.out.println(arr.length);

		@Data
		class ConstructorReference {
			int value;

			public ConstructorReference() {
				this.value = 1;
			}

			public ConstructorReference(int value) {
				this.value = value;
			}

			public ConstructorReference(int v1, int v2) {
				this.value = v1 + v2;
			}

		}

		Supplier<ConstructorReference> sp1 = ConstructorReference::new;
		ConstructorReference cf1 = sp1.get();
		System.out.println(cf1.getValue());
		IntFunction<ConstructorReference> if2 = ConstructorReference::new;
		ConstructorReference cf2 = if2.apply(10);
		System.out.println(cf2.getValue());
		BiFunction<Integer, Integer, ConstructorReference> bf3 = ConstructorReference::new;
		ConstructorReference cf3 = bf3.apply(10, 12);
		System.out.println(cf3.getValue());
	}

	@Test
	public void testStreamOperations1() {
		@Data
		@AllArgsConstructor
		class Property {
			String name;
			int distance;
			int sales;
			Integer priceLevel;
		}
		Property p1 = new Property("KFC", 1000, 500, 2);
		Property p2 = new Property("Dumpling", 2300, 1500, 3);
		Property p3 = new Property("Mcdonalds", 580, 3000, 1);
		Property p4 = new Property("Hilton", 6000, 200, 4);
		List<Property> properties = Arrays.asList(p1, p2, p3, p4);

		// sorted max min
		System.out.println(properties.stream().sorted(Comparator.comparingInt(Property::getDistance).reversed())
				.findFirst().get().getName());

		System.out.println(properties.stream().max((e1, e2) -> e1.getDistance() - e2.getDistance()).get().getName());

		// filter
		properties.stream().filter(e -> e.distance < 2000).filter(e -> e.priceLevel > 1).forEach(System.out::println);
		properties.stream().filter(e -> e.distance < 2000).map(Property::getName).forEach(System.out::println);

		// collector
		System.out.println(properties.stream().sorted(Comparator.comparingInt(Property::getDistance)).limit(2)
				.collect(Collectors.toList()));
		System.out.println(properties.stream().map(Property::getName).collect(Collectors.toList()));
		System.out.println(properties.stream().collect(Collectors.toMap(Property::getName, Property::getPriceLevel)));
		Map<Integer, List<Property>> groups = properties.stream()
				.collect(Collectors.groupingBy(Property::getPriceLevel));
		System.out.println(groups);
	}

	@Test
	public void testStreamOperations1WithNull() {
		@Data
		@AllArgsConstructor
		class Property {
			String name;
			int distance;
			int sales;
			Integer priceLevel;
		}
		Property p1 = new Property("KFC", 1000, 500, 2);
		Property p2 = new Property("Dumpling", 2300, 1500, 3);
		Property p3 = new Property("Mcdonalds", 580, 3000, 1);
		Property p4 = new Property("Hilton", 6000, 200, 4);
		List<Property> properties = Arrays.asList(p1, p2, p3, p4, null);

		// sorted max min
		System.out.println(properties.stream().filter(Objects::nonNull)
				.sorted(Comparator.comparingInt(Property::getDistance).reversed()).findFirst().get().getName());

		System.out.println(properties.stream().filter(Objects::nonNull)
				.max((e1, e2) -> e1.getDistance() - e2.getDistance()).get().getName());

		// filter
		properties.stream().filter(e -> e != null && e.distance < 2000).filter(e -> e.priceLevel > 1)
				.forEach(System.out::println);
		properties.stream().filter(e -> e != null && e.distance < 2000).map(Property::getName)
				.forEach(System.out::println);

		// collector
		System.out.println(properties.stream().filter(Objects::nonNull)
				.sorted(Comparator.comparingInt(Property::getDistance)).limit(2).collect(Collectors.toList()));
		System.out.println(
				properties.stream().filter(Objects::nonNull).map(Property::getName).collect(Collectors.toList()));
		System.out.println(properties.stream().filter(Objects::nonNull)
				.collect(Collectors.toMap(Property::getName, Property::getPriceLevel)));
		Map<Integer, List<Property>> groups = properties.stream()
				.collect(Collectors.groupingBy(Property::getPriceLevel));
		System.out.println(groups);

	}

	@Test
	public void testDefaultSort() {
		List<Integer> values = Arrays.asList(1, 3, 4, 2, 3, 1);
		System.out.println(values);
		// can not add values values.add(8);
		// Collections.sort(values);
		values.sort(null);
		System.out.println(values);
	}

	@Test
	public void testReadToString() throws IOException {
		String config = Files.readAllLines(Paths.get("src/test/resources/temp/in.txt")).stream()
				.collect(Collectors.joining(System.lineSeparator()));
		System.out.println(config);
	}

	@Test
	public void testFlatMap() {
		List<List<String>> lists = Arrays.asList(Arrays.asList("list1", "l", "s"), Arrays.asList("list2", "l", "s"),
				Arrays.asList("list3", "l", "s"));
		lists.stream().flatMap(Collection::stream).forEach(System.out::println);
	}
}
