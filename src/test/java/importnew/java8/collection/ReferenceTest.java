package importnew.java8.collection;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ReferenceTest {

	/**
	 * 软引用：对于软引用关联着的对象，在系统将要发生内存溢出异常之前，将会把这些对象列进回收范围之中进行第二次回收(
	 * 因为是在第一次回收后才会发现内存依旧不充足，才有了这第二次回收 )。如果这次回收还没有足够的内存，才会抛出内存溢出异常。
	 * 对于软引用关联着的对象，如果内存充足，则垃圾回收器不会回收该对象，如果内存不够了，就会回收这些对象的内存。
	 * 通过debug发现，软引用在pending状态时，referent就已经是null了。
	 *
	 * 启动参数：-Xmx5m
	 *
	 */
	@Test
	public void testSoftReference() {
		ReferenceQueue<MyObject> queue = new ReferenceQueue<>();
		MyObject object = new MyObject();
		SoftReference<MyObject> softRef = new SoftReference<>(object, queue);
		new Thread(() -> {
			Reference<? extends MyObject> obj = null;
			try {
				obj = queue.remove();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (obj != null) {
				System.out.println("Object for softReference is " + obj.get());
			}
		}).start();

		object = null;
		System.gc();
		System.out.println("After GC : Soft Get = " + softRef.get());
		System.out.println("分配大块内存");

		// byte[] b = new byte[5 * 1024 * 10];
		// byte[] b = new byte[5 * 1024 * 1024];
		System.out.println("After new byte[] : Soft Get = " + softRef.get());
	}

	/**
	 * 用来描述非必须的对象，但是它的强度比软引用更弱一些，被弱引用关联的对象只能生存
	 * 到下一次垃圾收集发送之前。当垃圾收集器工作时，无论当前内存是否足够，都会回收掉只被弱引用关联的对象。
	 * 一旦一个弱引用对象被垃圾回收器回收，便会加入到一个注册引用队列中。
	 */
	@Test
	public void testWeakReference() throws InterruptedException {
		ReferenceQueue<MyObject> queue = new ReferenceQueue<>();
		MyObject object = new MyObject();
		WeakReference<MyObject> softRef = new WeakReference<>(object, queue);
		new Thread(() -> {
			Reference<? extends MyObject> obj = null;
			try {
				obj = queue.remove();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (obj != null) {
				System.out.println("Object for weakReference is " + obj.get());
			}
		}).start();

		object = null;
		System.gc();
		System.out.println("After GC : Soft Get = " + softRef.get());
		System.out.println("分配大块内存");

		// byte[] b = new byte[5 * 1024 * 10];
		// byte[] b = new byte[5 * 1024 * 1024];
		System.out.println("After new byte[] : Soft Get = " + softRef.get());
		Thread.sleep(10000);
	}

	/**
	 * 虚引用也称为幽灵引用或者幻影引用，它是最弱的一种引用关系。一个持有虚引用的对象，和没有引用几乎是一样的，随时都有可能被垃圾回收器回收。
	 * 虚引用必须和引用队列一起使用，它的作用在于跟踪垃圾回收过程。
	 * 当phantomReference被放入队列时，说明referent的finalize()方法已经调用，并且垃圾收集器准备回收它的内存了。
	 */
	@Test
	public void testPhantomReference() throws InterruptedException {
		ReferenceQueue<MyObject> queue = new ReferenceQueue<>();
		MyObject object = new MyObject();
		PhantomReference<MyObject> phantomReference = new PhantomReference<>(object, queue);
		System.out.println(phantomReference);
		new Thread(() -> {
			Reference<? extends MyObject> obj = null;
			try {
				obj = queue.remove();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (obj != null) {
				System.out.println("Object for phantomReference is " + obj.get());
			}
		}).start();

		object = null;
		int i = 1;
		while (i < 10) {
			System.out.println("第" + i++ + "次GC");
			System.gc();
			TimeUnit.SECONDS.sleep(1);
		}
	}

	static class MyObject {
		@Override
		protected void finalize() throws Throwable {
			System.out.println("MyObject's finalize called");
			super.finalize();
		}

		@Override
		public String toString() {
			return "I am MyObject.";
		}
	}

}
