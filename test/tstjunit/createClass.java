import java.util.*;
import java.lang.reflect.*;



public class createClass {

	private static void CreateClass(String sname) {
		try {
			Class cls = Class.forName(sname);
			Class supercls = cls.getSuperclass();
			String modi = Modifier.toString(cls.getModifiers());
			if (modi.length() > 0) {
				System.out.printf(modi + " ");
			}
			System.out.printf("class " + sname);
			if (supercls != null && supercls != Object.class) {
				System.out.printf(" extends " + supercls.getName());
			}
			System.out.println("{");
			System.out.println("}");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String... args) {
		if (args.length > 0) {
			for (String s : args) {
				CreateClass(s);
			}
		}
	}
}