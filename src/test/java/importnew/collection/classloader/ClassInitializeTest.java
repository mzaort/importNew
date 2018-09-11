package importnew.collection.classloader;

public class ClassInitializeTest {
	static {
		print();
	}
	private static final double x = 123;// Math.random(); Different;
	static {
		System.out.println(x);
	}
	static void print() {
		System.out.println(x);
	}
	public static void main(String[] args) {
		ClassInitializeTest test = new ClassInitializeTest();
	}
}
