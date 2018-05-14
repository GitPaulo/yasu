package utilities;

import bsh.Interpreter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public final class BeanShellJava {
	public static String interpret(String code) {
		System.out.print(code);
		Interpreter i = new Interpreter();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		PrintStream stdout = System.out;
		System.setOut(ps);
		try {
			i.eval(code);
		} catch (Exception e) {
			return e.getMessage();
		}
		String out = baos.toString();
		System.setOut(stdout);

		return out;
	}
}
