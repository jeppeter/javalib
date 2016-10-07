
import java.util.Properties;
import java.util.Enumeration;


public class command {
	public static void main(String[] args) {
		int i;
		Properties p = System.getProperties();
		Enumeration keys = p.keys();
		while (keys.hasMoreElements()) {
		    String key = (String)keys.nextElement();
		    String value = (String)p.get(key);
		    System.out.println(key + ": " + value);
		}
		
		for ( String c:args) {
			System.out.println(String.format("%s",c));
		}
		return;
	}
}