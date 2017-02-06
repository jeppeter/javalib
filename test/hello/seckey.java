
import java.io.PrintStream;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.KeyGenerator;

public class seckey {
	public static void Usage(int ec,String fmt,Object... args) {
		PrintStream fout = System.err;
		if (ec == 0) {
			fout = System.out;
		}
		if (fmt != null) {
			fout.printf(fmt,args);
			fout.printf("\n");
		}

		fout.printf("seckey [command] [strings]\n");
		fout.printf("[command]:\n");
		fout.printf("\tseckey      [keys]           get security keys\n");
		fout.printf("\tkeygen                       to generate security keys\n");

		System.exit(ec);
	}

	public static int seckey_get(String basecode) {
		byte[] codebyte = Base64.getDecoder().decode(basecode);
		SecretKey origkey = new SecretKeySpec(codebyte,0,codebyte.length,"AES");
		String recode = Base64.getEncoder().encodeToString(origkey.getEncoded());
		System.out.printf("code [%s] encode [%s]\n",basecode,recode);
		return 0;
	}

	public static int keygen() throws Exception {
		SecretKey seckey = KeyGenerator.getInstance("AES").generateKey();
		String basecode = Base64.getEncoder().encodeToString(seckey.getEncoded());
		System.out.printf("code [%s]\n",basecode);
		return 0;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			seckey.Usage(3,"must specify command");
		}
		if (args[0].equals("seckey"))  {
			if (args.length < 2) {
				seckey.Usage(3,"[seckey]    [basecode]");
			}
			seckey.seckey_get(args[1]);
		} else if (args[0].equals("keygen")) {
			seckey.keygen();			
		} else {
			seckey.Usage(3,"unknown command [%s]",args[0]);
		}
		return;
	}
}