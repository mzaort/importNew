package importnew.java8.coding;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.UUID;

import org.junit.Test;

public class EncodeTest {

	@Test
	public void testBase64() {
		String text = "importnew.java8.coding";
		Decoder decoder = Base64.getDecoder();
		Encoder encoder = Base64.getEncoder();
		String eText = encoder.encodeToString(text.getBytes());
		String dText = new String(decoder.decode(eText));
		System.out.println(eText);
		System.out.println(dText);
	}

	@Test
	public void testBase64Continue() throws Exception {
		String str = "This is a String";
		String coding = "utf-8";
		String encodeToString = Base64.getEncoder().encodeToString(str.getBytes(coding));
		System.out.println(encodeToString);
		String decodeToString = new String(Base64.getDecoder().decode(encodeToString), coding);
		System.out.println(decodeToString);

		String url = "https://www.google.com/query?subject=7";
		encodeToString = Base64.getUrlEncoder().encodeToString(url.getBytes(coding));
		System.out.println(encodeToString);
		decodeToString = new String(Base64.getUrlDecoder().decode(encodeToString), coding);
		System.out.println(decodeToString);
		System.out.println(new String(Base64.getDecoder().decode(encodeToString), coding));

		StringBuilder sb = new StringBuilder();
		for (int t = 0; t < 10; ++t) {
			sb.append(UUID.randomUUID().toString());
		}
		byte[] toEncode = sb.toString().getBytes("utf-8");
		String mimeEncoded = Base64.getMimeEncoder().encodeToString(toEncode);
		System.out.println(mimeEncoded);
	}

}
