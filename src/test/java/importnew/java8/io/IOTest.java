package importnew.java8.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

public class IOTest {
	@Test
	public void testNewTryCatch() throws IOException {
		byte[] buffer = new byte[4 << 10];
		try (InputStream in = new FileInputStream("src/test/resources/temp/in.txt");
				OutputStream out = new FileOutputStream("src/test/resources/temp/out.txt")) {
			int length = 0;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
		}
	}

	@Test
	public void testCatchMultipleException() {
		try {
			Thread.sleep(10);
			new FileInputStream("src/test/resources/temp/not-exist.txt").close();
		} catch (InterruptedException | IOException e) {
			// FileNotFoundException is an instance of IOException
			// e.printStackTrace();
		}
	}

	@Test
	public void testNewPathsAndFiles() throws Exception {
		// Get path
		Path p1 = Paths.get("src/test/resources/temp/p1.txt");
		Path p2 = Paths.get("src/test/resources/temp", "p2.txt");
		Path p3 = FileSystems.getDefault().getPath("src/test/resources/temp", "p3.txt");
		Path setEnv = Paths.get(URI.create("file:///D:/Develop/Workspace/MZA100/set-env.bat"));

		Files.write(p1, "p1".getBytes());
		Files.write(p2, "p2".getBytes());
		Files.write(p3, "p3".getBytes());
		System.out.println(new String(Files.readAllBytes(setEnv)));

		// Transform
		File file = new File("src/test/resources/temp/p1.txt");
		Path filetopath = file.toPath();
		URI filetouri = file.toURI();

		Path path = Paths.get("src/test/resources/temp", "p4.txt");
		File pathtofile = path.toFile();
		URI pathtouri = path.toUri();

		URI uri = URI.create("file:///D:/Develop/Workspace/MZA100/set-env.bat");
		Path uritopath = Paths.get(uri);
		File uritofile = new File(uri);
		System.out.printf("T:%s,%s,%s,%s,%s,%s\n", filetopath, filetouri, pathtofile, pathtouri, uritopath, uritofile);

		// Read
		System.out.println(new String(Files.readAllBytes(p1)));
		System.out.println(Files.readAllLines(setEnv));

		// Write
		Files.write(path, "hello\nWorld".getBytes());
		System.out.println(Files.readAllLines(path));

		// To standard io
		InputStream inputStream = Files.newInputStream(filetopath);
		OutputStream outputStream = Files.newOutputStream(p3, StandardOpenOption.APPEND);
		outputStream.write(inputStream.read());

		BufferedReader reader = Files.newBufferedReader(setEnv);
		BufferedWriter writer = Files.newBufferedWriter(path);
		writer.write(reader.readLine());
		inputStream.close();
		outputStream.close();
		reader.close();
		writer.close();

		// File operation
		Path p6 = Paths.get("src/test/resources/temp/p6.txt");
		if (Files.notExists(p6)) {
			Files.createFile(p6);
		}

		Path p7 = Paths.get("src/test/resources/temp/pack/folder");
		if (Files.notExists(p7)) {
			Files.createDirectories(p7);
		}

		Path tmpFile = Files.createTempFile("go", ".tmp");
		Files.write(tmpFile, "go".getBytes());
		System.out.println(new String(Files.readAllBytes(tmpFile)));

		Path p8 = Paths.get("src/test/resources/temp/p8.txt");
		Files.copy(setEnv, p8, StandardCopyOption.REPLACE_EXISTING);
		Path p9 = Paths.get("src/test/resources/temp/p9.txt");
		Files.move(p8, p9, StandardCopyOption.REPLACE_EXISTING);
		Files.delete(p9);
		Files.deleteIfExists(p8);

		Path p10 = Paths.get("src/test/resources/temp");
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(p10)) {
			for (Path p : stream) {
				System.out.println(p);
			}
		}

		Files.list(Paths.get("C:/")).forEach(System.out::println);

		Files.walkFileTree(Paths.get("src/test/resources/temp"), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (file.getFileName().toString().startsWith("i")) {
					System.out.println(file);
				}
				return FileVisitResult.CONTINUE;
			}
		});

	}

}
