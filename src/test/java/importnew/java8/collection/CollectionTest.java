package importnew.java8.collection;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

public class CollectionTest {

	@Test
	public void testParallelArray() {
		long[] arrayOfLong = new long[20000];

		Arrays.parallelSetAll(arrayOfLong, index -> ThreadLocalRandom.current().nextInt(1000000));
		Arrays.stream(arrayOfLong).limit(10).forEach(i -> System.out.print(i + " "));
		System.out.println();

		Arrays.parallelSort(arrayOfLong);
		Arrays.stream(arrayOfLong).limit(10).forEach(i -> System.out.print(i + " "));
		System.out.println();
	}

}
