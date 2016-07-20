import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;

class Jsons {
	private String m_fname;
	private JSONObject m_objjson;
	public Jsons(String fname) {
		this.m_fname = fname;
	}

	public int parse() {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(this.m_fname));
			this.m_objjson = (JSONObject) obj;
		} Catch(FileNotFoundException e) {
			return -3;
		} Catch(IOException e) {
			return -4;
		} Catch(ParseException e) {
			return -5;
		}
		return 0;
	}

	public 
}


public class javaxmljson {
	public static void main(String... args) {
		JSONParser parser = new JSONParser();

	}
}