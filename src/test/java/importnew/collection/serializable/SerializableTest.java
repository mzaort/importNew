package importnew.collection.serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;

public class SerializableTest {

	@Test
	public void testReadResolve() {
		Person3 person = new Person3(100, "gogo");
		Path output = Paths.get("src/test/resources/temp/testReadResolve.bin");
		try (ObjectOutputStream fos = new ObjectOutputStream(Files.newOutputStream(output, StandardOpenOption.CREATE));
				ObjectInputStream fis = new ObjectInputStream(Files.newInputStream(output, StandardOpenOption.READ))) {
			fos.writeObject(person);
			Person3 person3 = (Person3) fis.readObject();
			System.out.println(person3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSealedObject() throws Exception {
		Employee em = new Employee("Matt", 10000);
		Path path = Paths.get("src/test/resources/temp/employee.save");
		Key key = null;

		// serialize object
		try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path, StandardOpenOption.CREATE))) {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
			key = keyGenerator.generateKey();
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			SealedObject so = new SealedObject(em, cipher);
			oos.writeObject(so);
			System.out.println("Serialized - " + em.toString());
		}

		// deserialize object
		try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
			SealedObject so = (SealedObject) ois.readObject();
			Employee e = (Employee) so.getObject(key);
			System.out.println("Deserialized - " + e.toString());
		}
	}

	@Test
	public void testBasicSerializableOverride() {
		Person2 person = new Person2(12, "all");
		Path output = Paths.get("src/test/resources/temp/testBasicSerializable.bin");

		try (ObjectOutputStream fos = new ObjectOutputStream(Files.newOutputStream(output, StandardOpenOption.CREATE));
				ObjectInputStream fis = new ObjectInputStream(Files.newInputStream(output, StandardOpenOption.READ))) {
			fos.writeObject(person);
			Person2 person2 = (Person2) fis.readObject();
			System.out.println(person2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBasicSerializable() {
		Person person = new Person(12, "all");
		Path output = Paths.get("src/test/resources/temp/testBasicSerializable.bin");

		try (ObjectOutputStream fos = new ObjectOutputStream(Files.newOutputStream(output, StandardOpenOption.CREATE));
				ObjectInputStream fis = new ObjectInputStream(Files.newInputStream(output, StandardOpenOption.READ))) {
			fos.writeObject(person);
			Person person2 = (Person) fis.readObject();
			System.out.println(person2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Data
	@AllArgsConstructor
	static class Person implements Serializable {
		private static final long serialVersionUID = 5213958003529249407L;
		int age;
		String name;
	}

	@Data
	@AllArgsConstructor
	static class Person2 implements Serializable {
		private static final long serialVersionUID = 5213958003529249407L;
		transient int age;
		transient String name;

		private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
			in.defaultReadObject();
			this.age = in.readInt() + 1024;
			this.name = "kkk";
		}
	}

	@Data
	@AllArgsConstructor
	static class Person3 implements Serializable {
		private static final long serialVersionUID = 5213958003529249407L;
		transient int age;
		transient String name;

		private Object writeReplace() {
			return new PersonProxy(this);
		}
	}

	@Data
	static class PersonProxy implements Serializable {
		private static final long serialVersionUID = -1090558833965055731L;
		public String data;

		public PersonProxy(Person3 orig) {
			data = String.format("%d-%s", orig.getAge(), orig.getName());
		}

		private Object readResolve() throws java.io.ObjectStreamException {
			String[] pieces = data.split("-");
			Person3 result = new Person3(Integer.parseInt(pieces[0]), pieces[1]);
			return result;
		}
	}

	@Data
	static class Employee implements Serializable {
		private static final long serialVersionUID = -7331553489509930824L;
		private String name;
		private double salary;

		public Employee(String name, double salary) {
			this.name = name;
			this.salary = salary;
		}
	}
}
