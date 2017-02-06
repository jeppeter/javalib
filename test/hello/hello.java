
public class hello {
	public static void printf(String fmt,Object... args) {
		System.out.printf(fmt,args);
	}

	public static void main(String args[]) {
		hello.printf("hello %s\n","world");
	} 
}