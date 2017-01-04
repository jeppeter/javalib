
import ;

public class base64code {

	static byte[] encode(String input) {

	}

	static void main(String[] args) {
		int i;
		if (args.length > 2) {
			if (args[0].equals("encode") == 0) {

			} else if (args[0].equals("decode")==0) {

			} else {
				System.err.printf("%s not supported\n",args[0]);
			}
		}
	}
}