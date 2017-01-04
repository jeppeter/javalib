
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class base64code {

	static String encode(String input) {
		BASE64Encoder encoder = new BASE64Encoder();
		byte[] b = input.getBytes();
		return encoder.encode(b);
	}

	static byte[] decode(String input) throws Exception {
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(input);
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

	static public void main(String[] args) throws Exception {
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