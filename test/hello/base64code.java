
import ;

public class base64code {

	static byte[] encode(String input) {
		
	}

	static byte[] decode(String input) {

	}

	static String format(byte[] bytes) {

	}

	static void main(String[] args) {
		int i;
		byte[] bytes;
		String fmtstr;
		if (args.length >= 2) {
			if (args[0].equals("encode") == 0) {
				for (i=1;i<args.length;i++) {
					bytes = encode(args[i]);
					fmtstr = format(bytes);
					System.out.printf("%s => %s\n",args[i],fmtstr);
				}
			} else if (args[0].equals("decode")==0) {
				for (i=1;i<args.length;i++) {
					bytes = decode(args[i]);
					fmtstr = format(bytes);
					System.out.printf("%s => %s\n",args[i],fmtstr);
				}
			} else {
				System.err.printf("%s not supported\n",args[0]);
			}
		}
	}
}