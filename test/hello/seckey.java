
public class seckey {
	public static void Usage(int ec,String fmt,...) {
		PrintStream fout = System.err;
		if (ec == 0) {
			fout = System.out;
		}
		if (fmt != null) {
			fout.Printf(fmt,)
		}

	}

	public static void main(String[] args) {
		if (args.length < 1) {

		}
	}
}