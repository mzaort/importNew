package importnew.java8.collection;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapTest {

	@Test
	public void testComputeIfAbsent() {
		Map<String, String> map = new HashMap<>();
		map.computeIfAbsent("a", e -> e + "1");
		System.out.println(map.getOrDefault("a", "b"));
	}

}
