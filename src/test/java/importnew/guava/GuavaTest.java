package importnew.guava;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.net.InternetDomainName;
import com.google.common.primitives.UnsignedInts;
import com.google.common.reflect.ClassPath;

public class GuavaTest {

	@Test
	public void testCharMatch() {
		System.out.println(CharMatcher.javaDigit().trimAndCollapseFrom("fj fsjf941jfsf21erhf98uf9283lsjdflsjo", '-'));
	}

	@Test
	public void testClasspath() throws IOException {
		ClassPath path = ClassPath.from(GuavaTest.class.getClassLoader());
		for (ClassPath.ClassInfo e : path.getTopLevelClasses("importnew.java8.concurrent")) {
			System.out.println(e.getName());
		}
	}

	@Test
	public void testInternetDomainName() {
		InternetDomainName blog = InternetDomainName.from("www.hello.com");
		System.out.println(blog.parts());
	}

	@Test
	public void testUnsigned() {
		int unint = UnsignedInts.parseUnsignedInt(Long.toString(20L + Integer.MAX_VALUE));
		System.out.println(unint);
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Integer.toUnsignedString(unint));
	}

	@Test
	public void testMurmurHash() {
		HashFunction hf = Hashing.murmur3_128();
		HashCode hash = hf.newHasher().putBoolean(true).putString("gogo", Charset.defaultCharset()).hash();
		System.out.println(hash.toString());
	}
}
