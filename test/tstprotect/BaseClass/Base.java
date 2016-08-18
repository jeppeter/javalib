package BaseClass;

import BaseClass.inner;

public class Base {
	public static void maincall() {
		inner.InnerCall();
		System.out.printf("maincall\n");
	}
}