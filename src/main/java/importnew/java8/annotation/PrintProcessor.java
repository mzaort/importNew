package importnew.java8.annotation;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class PrintProcessor extends AbstractProcessor {

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> set = new HashSet<String>(1);
		set.add(Printer.class.getName());
		return set;
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "start to use PrintProcessor ..");

		Set<? extends Element> rootElements = roundEnv.getRootElements();
		messager.printMessage(Diagnostic.Kind.NOTE, "root classes: ");
		for (Element root : rootElements) {
			messager.printMessage(Diagnostic.Kind.NOTE, ">> " + root.toString());
		}
		messager.printMessage(Diagnostic.Kind.NOTE, "annotation: ");
		for (TypeElement te : annotations) {
			messager.printMessage(Diagnostic.Kind.NOTE, ">>> " + te.toString());
			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(te);
			for (Element ele : elements) {
				messager.printMessage(Diagnostic.Kind.NOTE, ">>>> " + ele.toString());
			}
		}

		return true;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
}