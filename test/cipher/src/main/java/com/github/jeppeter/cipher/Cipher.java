package com.github.jeppeter.cipher;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;

import java.util.Base64;


import com.github.jeppeter.extargsparse4j.Parser;
import com.github.jeppeter.extargsparse4j.NameSpaceEx;

public class Cipher {
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

	static byte[] encrypt_cipher(String algorithm,String fmt,String inputkey,byte[] inputbytes) throws Exception {
		byte[] retbytes;
		byte[] encodekey;
		Object val;
		SecretKeySpec keyspec;
		if (inputkey == null) {
			keyspec = new SecretKeySpec();
		}

		return retbytes;
	}

	static byte[] decrypt_cipher(String algorithm,String fmt,String inputkey,byte[] inputbytes) throws Exception {

	}

	static void main(String[] args) throws Exception {
		String commandline = "";
		Parser parser = new Parser();
		NameSpaceEx ns;
		String subcommand;
		commandline += String.format("{\n");
		commandline += String.format("\"verbose\" : \"+\",\n");
		commandline += String.format("\"algorithm|A##algorithm for encrypt or decrypt (AES|DES|DES3)##\" : null,\n");
		commandline += String.format("\"format|F##format for encrypt or decrypt (AES/CFB/NoPadding|AES/ECB/NoPadding)##\" : null,\n");
		commandline += String.format("\"key|k##to get key in##\" : [],\n");
		commandline += String.format("\"encrypt\" : { \"$\" : \"+\"},\n");
		commandline += String.format("\"decrypt\" : { \"$\" : \"+\"}");
		commandline += String.format("}\n");
		parser.load_command_line_string(commandline);
		ns = parser.parse_command_line(args,null);
		subcommand = (String) ns.getObject("subcommand");
		if (subcommand.equals("encrypt")) {

		} else if (subcommand.equals("decrypt")) {

		}
	}
}