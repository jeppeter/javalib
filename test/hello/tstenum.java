
import enumval.Color;

public class tstenum {
	private static void debug_enum(Color[] cls){
		for (Color c : cls) {
			System.out.println(String.format("("+c+")"));
		}
	}

	public static void main(String [] args){
		Color[] cls = {Color.RED,Color.BLACK};
		debug_enum(cls);
		return;
	}
}