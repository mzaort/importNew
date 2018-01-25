package importnew.java8.annotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * Class SimplePackageScanner is a package scanner which implements interface
 * PackageScanner and it offers functionally very simple.
 *
 * Created by SylvanasSun on 10/13/2017.
 */
@Slf4j
public class SimplePackageScanner implements PackageScanner {

	protected String packageName;

	protected String packagePath;

	protected ClassLoader classLoader;

	public SimplePackageScanner() {
		this.classLoader = Thread.currentThread().getContextClassLoader();
	}

	@Override
	public List<Class<?>> scan(String packageName) {
		return this.scan(packageName, null);
	}

	@Override
	public List<Class<?>> scan(String packageName, ScannedClassHandler handler) {
		this.initPackageNameAndPath(packageName);
		if (log.isDebugEnabled()) log.debug("Start scanning package: {} ....", this.packageName);
		URL url = this.getResource(this.packagePath);
		if (url == null) return new ArrayList<>();
		return this.parseUrlThenScan(url, handler);
	}

	private void initPackageNameAndPath(String packageName) {
		this.packageName = packageName;
		this.packagePath = PathUtils.packageToPath(packageName);
	}

	protected URL getResource(String packagePath) {
		URL url = this.classLoader.getResource(packagePath);
		if (url != null) log.debug("Get resource: {} success!", packagePath);
		else log.debug("Get resource: {} failed,end of scan.", packagePath);
		return url;
	}

	protected List<Class<?>> parseUrlThenScan(URL url, ScannedClassHandler handler) {
		String urlPath = "";
		try {
			urlPath = PathUtils.getUrlMainPath(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.debug("Get url path failed.");
		}

		// decide file type
		ResourceType type = PathUtils.getResourceType(url);
		List<Class<?>> classList = new ArrayList<>();

		try {
			switch (type) {
			case FILE:
				classList = this.getClassListFromFile(urlPath, this.packageName);
				break;
			case JAR:
				classList = this.getClassListFromJar(urlPath);
				break;
			default:
				log.debug("Unsupported file type.");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			log.debug("Get class list failed.");
		}

		this.invokeCallback(classList, handler);
		log.debug("End of scan <{}>.", urlPath);
		return classList;
	}

	protected List<Class<?>> getClassListFromFile(String path, String packageName) throws ClassNotFoundException {
		File file = new File(path);
		List<Class<?>> classList = new ArrayList<>();

		File[] listFiles = file.listFiles();
		if (listFiles != null) {
			for (File f : listFiles) {
				if (f.isDirectory()) {
					List<Class<?>> list = getClassListFromFile(f.getAbsolutePath(),
							PathUtils.concat(packageName, ".", f.getName()));
					classList.addAll(list);
				} else if (PathUtils.isClassFile(f.getName())) {
					// only add class file that not contain "$"
					String className = PathUtils.trimSuffix(f.getName());
					if (-1 != className.lastIndexOf("$")) continue;

					String finalClassName = PathUtils.concat(packageName, ".", className);
					classList.add(Class.forName(finalClassName));
				}
			}
		}

		return classList;
	}

	protected List<Class<?>> getClassListFromJar(String jarPath) throws IOException, ClassNotFoundException {
		if (log.isDebugEnabled()) log.debug("Start scanning jar: {}", jarPath);

		JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarPath));
		JarEntry jarEntry = jarInputStream.getNextJarEntry();
		List<Class<?>> classList = new ArrayList<>();

		while (jarEntry != null) {
			String name = jarEntry.getName();
			if (name.startsWith(this.packageName) && PathUtils.isClassFile(name)) classList.add(Class.forName(name));
			jarEntry = jarInputStream.getNextJarEntry();
		}

		jarInputStream.close();

		return classList;
	}

	protected void invokeCallback(List<Class<?>> classList, ScannedClassHandler handler) {
		if (classList != null && handler != null) {
			for (Class<?> clazz : classList) {
				handler.execute(clazz);
			}
		}
	}

}