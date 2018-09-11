package importnew.java8.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Sum extends RecursiveTask<Integer> {

	private static final int THRESHOLD = 2;
	private int start;
	private int end;

	@Override
	protected Integer compute() {
		if (end - start <= THRESHOLD) {
			int r = 0;
			for (int i = start; i <= end; i++) {
				r += i;
			}
			return r;
		} else {
			int mid = start + ((end - start) >>> 1);
			Sum left = new Sum(start, mid);
			Sum right = new Sum(mid + 1, end);
			invokeAll(left, right);
			// left.fork();
			// right.fork();
			return left.join() + right.join();
		}
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ForkJoinPool newWorkStealingPool = new ForkJoinPool();
		for (int i = 0; i < 100; i++) {
			int start = (int) (1000 * Math.random());
			int end = (int) (1000 * Math.random());
			if (start > end) {
				start = start ^ end;
				end = start ^ end;
				start = start ^ end;
			}
			ForkJoinTask<Integer> res = newWorkStealingPool.submit(new Sum(start, end));
			int val = res.get();
			if (val != (start + end) * (end - start + 1) / 2) {
				System.out.println(res.get());
			}
		}
	}

}
