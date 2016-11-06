

import java.lang.StackTraceElement;
import java.lang.Thread;

public class callstack {
	public callstack() {

	}

	public void search_stack() {
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		int i=0;
		for (StackTraceElement c : stacks) {
			System.out.printf("[%d]%s\n",i,c.toString());
			i ++;
		}
		return;
	}
	public static void main(String[] args) {
		callstack stk = new callstack();
		stk.search_stack();
		return;
	}
}