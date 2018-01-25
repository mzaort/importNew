package importnew.java8.concurrent;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

public class CompletableFutureTest {

	@Test
	public void testCompletableFutureSimple() throws InterruptedException {
		CompletableFuture<Integer> future = new CompletableFuture<>();
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("in thread");
				future.complete(1000);
			}
		};
		t.start();
		System.out.println("in main");
		future.thenAccept(System.out::println);
		t.join();
	}

	@Test
	public void testCompletableFutureFactoryMethod() {
		class ValueGenerator {
			int generate() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return Double.valueOf(1000 * Math.random()).intValue();
			}
		};

		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new ValueGenerator()::generate);
		System.out.println("before");
		future.thenAccept(System.out::println);
		System.out.println("after");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
