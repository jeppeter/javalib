package com.github.jeppeter.cipher;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import javax.crypto.spec.IvParameterSpec;
import java.util.List;

import java.util.Base64;


import com.github.jeppeter.extargsparse4j.Parser;
import com.github.jeppeter.extargsparse4j.NameSpaceEx;

public class CipherApp {
	static String format_byte(Byte[] bytes) {
		String retstr="";
		int i;
		for (i=0;i<bytes.length;i++) {
			if ((i %16)==0) {
				if (i > 0) {
					retstr += String.format("\n");
				}
				retstr += String.format("0x%08x",i);
			}
			retstr += String.format(" 0x%02x",bytes[i]);
		}
		return retstr;
	}

	static byte[] decode_base64(String input) throws Exception {
		return Base64.getDecoder().decode(input);
	}

	static String encode_base64(byte[] inputbytes) {
		return Base64.getEncoder().encodeToString(inputbytes);
	}

	static byte[] encrypt(Cipher enc,byte[] inputbytes) {
		return enc.update(inputbytes,0,inputbytes.length);
	}

	static byte[] decrypt(Cipher dec,byte[] inputbytes) {
		return dec.update(inputbytes,0,inputbytes.length);
	}

	static int get_algo_keybytes(String algo) throws Exception {
		if (algo.equals("AES")) {
			return (128 / 8);
		} 
		throw new Exception(String.format("unkown algo (%s)",algo));
	}

	static String get_random_bytes(String algo) throws Exception {
		int bytenum;
		byte[] iv;
		SecureRandom random = new SecureRandom();
		bytenum = get_algo_keybytes(algo);
		iv = new byte[bytenum];
		random.nextBytes(iv);
		return encode_base64(iv);
	}

	static Cipher get_cipher(String algo,String fmt,String inputkey,String initval,int mode) throws Exception {
		Cipher retcipher = Cipher.getInstance(fmt);
		SecretKey seckey = null;
		byte[] inputcode;
		String setval;

		if (inputkey != null && initval != null) {
			inputcode = decode_base64(inputkey);
			seckey = new SecretKeySpec(inputcode,0,inputcode.length,algo);
			retcipher.init(mode,seckey,new IvParameterSpec(decode_base64(initval)));
		} else if (initval != null) {
			seckey = KeyGenerator.getInstance(algo).generateKey();
			System.err.printf("seckey [%s]\n",Base64.getEncoder().encodeToString(seckey.getEncoded()));
			retcipher.init(mode,seckey,new IvParameterSpec(decode_base64(initval)));
		} else {
			seckey = KeyGenerator.getInstance(algo).generateKey();
			setval = get_random_bytes(algo);
			System.err.printf("seckey [%s]\n",Base64.getEncoder().encodeToString(seckey.getEncoded()));
			System.err.printf("initval [%s]\n",setval);
			retcipher.init(mode,seckey,new IvParameterSpec(decode_base64(setval)));
		}
		return retcipher;
	}

	static Cipher get_enccipher(String algorithm,String fmt,String inputkey,String initval) throws Exception {
		return get_cipher(algorithm,fmt,inputkey,initval,Cipher.ENCRYPT_MODE);
	}

	static Cipher get_deccipher(String algorithm,String fmt,String inputkey,String initval) throws Exception {
		return get_cipher(algorithm,fmt,inputkey,initval,Cipher.DECRYPT_MODE);
	}

	public static int encrypt_handler(NameSpaceEx ns,Object parser) throws Exception {
		Cipher enc;
		String encstr;
		byte[] encbytes;
		byte[] decbytes;
		int i;
		List<String> subnargs;
		if (ns.getString("algorithm") == null) {
			throw new Exception(String.format("no algorithm specified"));
		}
		if (ns.getString("format") == null) {
			throw new Exception(String.format("no format specified"));
		}
		enc = get_enccipher(ns.getString("algorithm"),ns.getString("format"),ns.getString("key"),ns.getString("initval"));
		subnargs = (List<String>) ns.get("subnargs");
		for(i=0;i<subnargs.size();i++) {
			decbytes = decode_base64(((String)subnargs.get(i)));
			encbytes = encrypt(enc,decbytes);
			encstr = encode_base64(encbytes);
			System.out.printf("[%s] encrypt [%s]\n",(String)subnargs.get(i),encstr);
		}
		return 0;
	}

	public static int decrypt_handler(NameSpaceEx ns,Object parser) throws Exception {
		Cipher dec;
		String decstr;
		byte[] encbytes;
		byte[] decbytes;
		int i;
		List<String> subnargs;
		if (ns.getString("algorithm") == null) {
			throw new Exception(String.format("no algorithm specified"));
		}
		if (ns.getString("format") == null) {
			throw new Exception(String.format("no format specified"));
		}
		dec = get_deccipher(ns.getString("algorithm"),ns.getString("format"),ns.getString("key"),ns.getString("initval"));
		subnargs = (List<String>) ns.get("subnargs");
		for(i=0;i<subnargs.size();i++) {
			encbytes = decode_base64(((String)subnargs.get(i)));
			decbytes = decrypt(dec,encbytes);
			decstr = encode_base64(decbytes);
			System.out.printf("[%s] decrypt [%s]\n",(String)subnargs.get(i),decstr);
		}
		return 0;
	}



	public static void main(String[] args) throws Exception {
		String commandline = "";
		Parser parser = new Parser();
		NameSpaceEx ns;
		String subcommand;
		commandline += String.format("{\n");
		commandline += String.format("\"verbose\" : \"+\",\n");
		commandline += String.format("\"algorithm|A##algorithm for encrypt or decrypt (AES|DES|DES3)##\" : null,\n");
		commandline += String.format("\"format|F##format for encrypt or decrypt (AES/CFB/NoPadding|AES/ECB/NoPadding)##\" : null,\n");
		commandline += String.format("\"key|k##to get key in##\" : null,\n");
		commandline += String.format("\"initval|i##to set init value##\": null,\n");
		commandline += String.format("\"encrypt\" : { \"$\" : \"+\"},\n");
		commandline += String.format("\"decrypt\" : { \"$\" : \"+\"}");
		commandline += String.format("}\n");
		parser.load_command_line_string(commandline);
		ns = parser.parse_command_line(args,null);
		subcommand = (String) ns.get("subcommand");
		if (subcommand.equals("encrypt")) {
			encrypt_handler(ns,parser);
		} else if (subcommand.equals("decrypt")) {
			decrypt_handler(ns,parser);
		}
		return;
	}
}