package com.github.jeppeter.cipher;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import sun.misc.BASE64Decoder; 
import sun.misc.BASE64Encoder; 
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

	static Byte[] encrypt_cipher(NameSpaceEx ns) throws Exception {
		Byte[] bytes;

		return bytes;
	}

	static void main(String[] args) throws Exception {
		String commandline = "";
		Parser parser = new Parser();
		NameSpaceEx ns;
		commandline += String.format("{\n");
		commandline += String.format("\"verbose\" : \"+\",\n");
		commandline += String.format("\"algorithm|A##algorithm for encrypt or decrypt (AES|DES|DES3)##\" : null,\n");
		commandline += String.format("\"format|F##format for encrypt or decrypt (AES/CFB/NoPadding|AES/ECB/NoPadding)##\" : null,\n");
		commandline += String.format("\"key|k##to get key in##\" : []\n");
		commandline += String.format("}\n");
		parser.load_command_line_string(commandline);
		ns = parser.parse_command_line(args,null);


	}
}