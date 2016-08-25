
import java.util.List;
import java.util.ArrayList;

public class typeget {
	public static void main(String... args) {
		List<String> lstr = new ArrayList<String>();
		String str = "hello";
		System.out.printf("lstr type (%s)\n",lstr.getClass().getName());
		System.out.printf("str type (%s)\n",str.getClass().getName());
		return;
	}
}