package com.github.jeppeter.jsonext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import com.github.jeppeter.reext.ReExt;

import com.github.jeppeter.jsonext.JsonExtInvalidTypeException;
import com.github.jeppeter.jsonext.JsonExtNotFoundException;
import com.github.jeppeter.jsonext.JsonExtNotParsedException;

import java.util.Set;

public class JsonExt {
	private JSONObject m_object;
	public JsonExt() {
		this.m_object = null;
	}

	public void Clone(Object value) throws JsonExtInvalidTypeException {
		if (value == null){
			this.m_object = null;
		} else if (value instanceof JSONObject) {
			this.m_object = (JSONObject) value;
		}  else {
			throw new JsonExtInvalidTypeException(String.format("not valid type (%s)",value.getClass().getName()));
		}
		return;
	}

	public boolean parseFile(String file) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			this.m_object = (JSONObject)obj;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public boolean parseString(String str) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(str);
			this.m_object = (JSONObject)obj;
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	private Object __getObject(String path) throws JsonExtNotParsedException,JsonExtNotFoundException,JsonExtInvalidTypeException {
		String[] retstr;
		String curpath;
		int i;
		Object curobj;
		JSONObject jobj;
		if (this.m_object == null) {
			throw new JsonExtNotParsedException("not parsed yet");
		}
		retstr = ReExt.Split("\\/", path);
		if (retstr.length < 1) {
			return this.m_object;
		}

		curobj = (Object)this.m_object;
		for (i = 0; i < retstr.length; i++) {
			curpath = retstr[i];
			if (curpath.length() == 0) {
				continue;
			}
			if (!(curobj instanceof JSONObject)) {
				throw new JsonExtInvalidTypeException(String.format("(%s:%s) not JSONObject", path, curpath));
			}
			jobj = (JSONObject) curobj;

			if (!jobj.containsKey(curpath)) {
				throw new JsonExtNotFoundException(String.format("can not find (%s) at (%s)", path, curpath));
			}			
			curobj = (Object) jobj.get(curpath);
		}
		return curobj;
	}

	private String[] __getKeys(String path) throws JsonExtNotParsedException,JsonExtNotFoundException,JsonExtInvalidTypeException {
		String[] retstr;
		String curpath;
		int i;
		Object curobj;
		JSONObject jobj;
		Set<String> keys;

		if (this.m_object == null) {
			throw new JsonExtNotParsedException("not parsed yet");
		}
		retstr = ReExt.Split("\\/", path);
		if (retstr.length < 1) {
			keys = (Set<String>) this.m_object.keySet();
			return (String[])keys.toArray(new String[this.m_object.size()]);
		}

		curobj = (Object)this.m_object;
		for (i = 0; i < retstr.length; i++) {
			curpath = retstr[i];
			if (curpath.length() == 0) {
				continue;
			}
			if (!(curobj instanceof JSONObject)) {
				throw new JsonExtInvalidTypeException(String.format("(%s:%s) not JSONObject", path, curpath));
			}
			jobj = (JSONObject) curobj;

			if (!jobj.containsKey(curpath)) {
				throw new JsonExtNotFoundException(String.format("can not find (%s) at (%s)", path, curpath));
			}			
			curobj = (Object) jobj.get(curpath);
		}

		if (!(curobj instanceof JSONObject)) {
			throw new JsonExtInvalidTypeException(String.format("(%s)last not JSONObject(%s)",path,curobj.getClass().getName()));
		}
		jobj = (JSONObject) curobj;
		keys = (Set<String>)jobj.keySet();

		return (String[]) keys.toArray(new String[jobj.size()]);
	}

	public Long getLong(String path) throws JsonExtNotParsedException,JsonExtNotFoundException,JsonExtInvalidTypeException {
		Object obj;

		obj = this.__getObject(path);
		if (obj instanceof Long) {
			return  (Long)obj;
		}

		throw new JsonExtInvalidTypeException(String.format("(%s) not Long(%s)", path,obj.getClass().getName()));
	}

	public String getString(String path) throws JsonExtNotParsedException,JsonExtNotFoundException,JsonExtInvalidTypeException {
		Object obj;
		obj = this.__getObject(path);
		if (obj instanceof String) {
			return (String) obj;
		}
		throw new JsonExtInvalidTypeException(String.format("(%s) not String(%s)", path,obj.getClass().getName()));
	}

	public String[] getKeys(String path) throws JsonExtNotParsedException,JsonExtNotFoundException,JsonExtInvalidTypeException {

		return this.__getKeys(path);
	}


	public Object getObject(String path) throws JsonExtNotParsedException,JsonExtNotFoundException,JsonExtInvalidTypeException {
		return this.__getObject(path);
	}
}
