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
		} catch(FileNotFoundException e) {
			return -3;
		} catch(IOException e) {
			return -4;
		} catch(ParseException e) {
			return -5;
		}
		return 0;
	}

	public HashMap<String,Object> get() {
		HashMap<String,Object> obj = new HashMap<>();
		for (Iterator iter = this.m_objjson.keySet().Iterator();iter.hasNext();){
			String key = (String) iter.next();
			Object o = this.m_objjson.get(key);
			System.out.println("key " + key + "=" + o);
		}
		return obj;
	}
}


public class javaxmljson {
	public static void main(String... args) {
		for (String s : args) {
			Jsons j = new Jsons(s);
			j.parse();
			j.get();

		}
	}
}