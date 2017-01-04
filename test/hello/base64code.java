
import java.util.Base64;

public class base64code {

	static String encode(String input) {
		Base64.Encoder encoder;
		byte[] b = input.getBytes();
		encoder = Base64.getEncoder();
		return encoder.encodeToString(b);
	}

	static byte[] decode(String input) {
		Base64.Decoder decoder;
		decoder = Base64.getDecoder();
		return decoder.decode(input);
	}

	static String format(byte[] bytes) {
		String retstr = "[";
		int i;
		for (i=0;i<bytes.length;i++) {
			if (i > 0) {
				retstr += ",";
			}
			retstr += String.format("0x%02x",bytes[i]);
		}
		retstr += "]";
		return retstr;
	}

	static public void main(String[] args) {
		int i;
		byte[] bytes;
		String fmtstr;
		if (args.length >= 2) {
			if (args[0].equals("encode")) {
				for (i=1;i<args.length;i++) {
					fmtstr = encode(args[i]);
					System.out.printf("%s => %s\n",args[i],fmtstr);
				}
			} else if (args[0].equals("decode")) {
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