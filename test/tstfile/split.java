

public class split{
	public static void main(String... args) {
		String s = "CallOptions.CallOptionsMain";
		String[] paths = s.split("\\.");
		int i;
		for (String es :paths) {
			System.out.printf("%s\n",es);
		}
		System.out.println("length " + paths.length);
	}
}