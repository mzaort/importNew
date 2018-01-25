package importnew.java8.annotation;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class SimplePackageScannerTest {

	@Test
	public void testScanPackage() {
		SimplePackageScanner scanner = new SimplePackageScanner();
		List<Class<?>> scan = scanner.scan("importnew.java8.annotation");
		System.out.println(scan);

		scan = scanner.scan("importnew.java8");
		System.out.println(scan);
	}

	@Test
	public void testScanJar() {
		SimplePackageScanner scanner = new SimplePackageScanner();
		File file = new File("asm-tree");
		List<Class<?>> scan = scanner.scan(file.toURI() + "!/org/objectweb/asm");
		System.out.println(scan);
	}

}
