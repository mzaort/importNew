package importnew.java8.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

public class JavascriptTest {

	@Test
	public void testEval() throws ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		System.out.println(engine.getClass().getName());
		System.out.println("Result:" + engine.eval("function f() { return 1; }; f() + 1;"));
	}

}
