package com.github.jeppeter.jsonext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import reext.ReExt;

import com.github.jeppeter.jsonext.JsonExtInvalidTypeException;
import com.github.jeppeter.jsonext.JsonExtNotFoundException;
import com.github.jeppeter.jsonext.JsonExtNotParsedException;


public class JsonExt {
	private JSONObject m_object;
	public JsonExt() {
		this.m_object = null;
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

	public Long getLong(String path) throws JsonExtNotParsedException,JsonExtNotFoundException,JsonExtInvalidTypeException {
		Object obj;
		try{
			obj = this.__getObject(path);
		}
		catch (JsonExtNotFoundException e) {
			throw e;
		}
		catch (JsonExtNotParsedException e) {
			throw e;
		}
		catch (JsonExtInvalidTypeException e) {
			throw e;
		}
		if (obj instanceof Long) {
			return  (Long)obj;
		}

		throw new JsonExtInvalidTypeException(String.format("(%s) not Long(%s)", path,obj.getClass().getName()));
	}

	public String getString(String path) throws JsonExtNotParsedException,JsonExtNotFoundException,JsonExtInvalidTypeException {
		Object obj;
		try{
			obj = this.__getObject(path);
		}
		catch (JsonExtNotFoundException e) {
			throw e;
		}
		catch (JsonExtNotParsedException e) {
			throw e;
		}
		catch (JsonExtInvalidTypeException e) {
			throw e;
		}
		if (obj instanceof String) {
			return (String) obj;
		}
		throw new JsonExtInvalidTypeException(String.format("(%s) not String(%s)", path,obj.getClass().getName()));
	}


}
