import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.lang.Class;

class JsonNotFoundException extends Exception {
	public JsonNotFoundException(String expr) {
		super(expr);
	}
}

class JSONEncap  {
	private String m_fname;
	private JSONObject m_objjson;
	public JSONEncap(String fname) {
		this.m_fname = fname;
	}

	public int parse() {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(this.m_fname));
			this.m_objjson = (JSONObject) obj;
		} catch (FileNotFoundException e) {
			return -3;
		} catch (IOException e) {
			return -4;
		} catch (ParseException e) {
			return -5;
		}
		return 0;
	}

	public Object getvalue(String path) throws JsonNotFoundException {
		Object obj = null;
		Object curobj = null;
		JSONObject curhash;
		String[] pathexts;
		pathexts = path.split("/");

		curobj = this.m_objjson;
		for (String curpath : pathexts) {
			if (curpath.length() == 0) {
				continue;
			}

			if (!(curobj instanceof JSONObject)) {
				throw new JsonNotFoundException("Not found " + path + " can not down (" + curpath + ") key");
			}

			curhash = (JSONObject) curobj;

			if (!curhash.containsKey(curpath)) {
				throw new JsonNotFoundException("Not found " + path + " at (" + curpath + ") key");
			}

			curobj = curhash.get(curpath);
		}

		return curobj;
	}
}


public class javaxmljson {
	public static void main(String... args) {
		for (String s : args) {
			JSONEncap j = new JSONEncap(s);
			Object val;
			j.parse();
			try {
				val = j.getvalue("/good/attr");
				System.out.printf("get value (%s)\n", val);
			} catch (JsonNotFoundException e) {
				System.out.println(e);
			}

		}
	}
}