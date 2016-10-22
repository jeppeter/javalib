


public class getenv {
	public static void main(String[] args) {
		for (String c : args) {
			System.out.println(c + "="+ System.getenv(c));
		}
	}
}