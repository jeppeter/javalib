
import java.util.Base64;


public class base64code {

	static String encode(String input) {
		byte[] b = input.getBytes();
		return Base64.getEncoder().encodeToString(b);
	}

	static String encode(byte[] inputbytes) {
		return Base64.getEncoder().encodeToString(inputbytes);
	}

	static String decode(String input) throws Exception {
		return new String(Base64.getDecoder().decode(input),"UTF-8");
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
					fmtstr = decode(args[i]);
					System.out.printf("%s => %s\n",args[i],fmtstr);
				}
			}  else if (args[0].equals("encodebyte")) {
				bytes = new byte[args.length-1];
				int num;
				for (i=1;i<args.length;i++) {
					num = Integer.parseInt(args[i]);
					bytes[i-1] = (byte)num;
				}
				System.out.printf("%s => %s\n",format(bytes),encode(bytes));
			} else {
				System.err.printf("%s not supported\n",args[0]);
			}
		}
	}
}