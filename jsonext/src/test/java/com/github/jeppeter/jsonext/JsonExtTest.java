package com.github.jeppeter.jsonext;

import com.github.jeppeter.jsonext.JsonExtInvalidTypeException;
import com.github.jeppeter.jsonext.JsonExtNotFoundException;
import com.github.jeppeter.jsonext.JsonExtNotParsedException;
import com.github.jeppeter.jsonext.JsonExt;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import static org.junit.Assert.assertEquals;
import org.junit.*;


public class JsonExtTest {
	@Test
	public void test_A001() throws JsonExtNotParsedException, JsonExtNotFoundException, JsonExtInvalidTypeException {
		JsonExt json = new JsonExt();
		boolean bret;
		String value = "";

		bret = json.parseString("{\"name\" : \"jack\"}");
		assertEquals("parse ok", bret, true);
		bret = true;
		value = json.getString("name");
		assertEquals("get name ok", bret, true);
		assertEquals("get name (jack)", "jack", value);
		return;
	}

	@Test
	public void test_A002() throws JsonExtNotParsedException, JsonExtNotFoundException, JsonExtInvalidTypeException {
		JsonExt json = new JsonExt();
		boolean bret;
		String value = "";
		String key = "person/name";

		bret = json.parseString("{\"person\" :{ \"name\" :\"jack\"}}");
		assertEquals("parse ok", bret, true);
		bret = true;
		value = json.getString(key);
		assertEquals(String.format("get %s ok", key), bret, true);
		assertEquals(String.format("get %s (jack)", key), "jack", value);
		return;
	}

	@Test
	public void test_A003() throws JsonExtNotParsedException, JsonExtNotFoundException, JsonExtInvalidTypeException {
		JsonExt json = new JsonExt();
		boolean bret;
		Long value = new Long(0);
		String key = "person/age";

		bret = json.parseString("{\"person\" :{ \"age\" :13}}");
		assertEquals("parse ok", bret, true);
		bret = true;
		value = json.getLong(key);
		assertEquals(String.format("get %s ok", key), bret, true);
		assertEquals(String.format("get %s (13)", key), new Long(13), value);
		return;
	}


	@Test
	public void test_A004() throws JsonExtNotParsedException, JsonExtNotFoundException, JsonExtInvalidTypeException {
		JsonExt json = new JsonExt();
		boolean bret;
		String[] keys;
		String key = "person";

		bret = json.parseString("{\"person\" :{ \"age\" :13}}");
		assertEquals("parse ok", bret, true);
		keys = json.getKeys(key);
		assertEquals(String.format("getkeys %s (1)", key), keys.length,1);
		assertEquals(String.format("getkeys %s[0] (age)",key),keys[0],"age");
		return;
	}

	@Test
	public void test_A005() throws JsonExtNotParsedException, JsonExtNotFoundException, JsonExtInvalidTypeException {
		JsonExt json = new JsonExt();
		boolean bret;
		String key = "person";
		Object value;
		bret = json.parseString("{\"person\" :{ \"age\" :13}}");
		assertEquals("parse ok",bret,true);
		value = json.getObject(key);
		assertEquals(String.format("object %s",value.getClass().getName()),value instanceof JSONObject,true);
	}

	@Test
	public void test_A006() throws JsonExtNotParsedException, JsonExtNotFoundException, JsonExtInvalidTypeException {
		JsonExt json = new JsonExt();
		boolean bret;
		String key = "person/relations";
		Object value;
		//bret = json.parseString("{\"person\" :{ \"age\" :13 , \"relations\":[\"mother\",\"father\"]},\"newvalue\": null,\"salary\": 3.22, \"boolvalue\": false"});
		bret = json.parseString("{\"person\" :{ \"age\" :13 , \"relations\":[\"mother\",\"father\"]},\"newvalue\": null,\"salary\": 3.22, \"boolvalue\": false}");
		assertEquals("parse ok",bret,true);
		value = json.getObject(key);
		assertEquals(String.format("object %s",value.getClass().getName()),value instanceof JSONArray,true);
		value = json.getObject("newvalue");
		assertEquals(String.format("newvalue null"),value,null);
		value = json.getObject("boolvalue");
		assertEquals(String.format("boolvalue %s",value.getClass().getName()),value instanceof Boolean,true);
		value = json.getObject("salary");
		assertEquals(String.format("salary %s",value.getClass().getName()),value instanceof Double,true);
	}

	@Test
	public void test_A007() throws JsonExtNotParsedException, JsonExtNotFoundException, JsonExtInvalidTypeException {
		JsonExt json = new JsonExt();
		JsonExt jsonext;
		Object obj;
		boolean bret;
		String key = "person/relations";
		Object value;
		String[] keys; 
		//bret = json.parseString("{\"person\" :{ \"age\" :13 , \"relations\":[\"mother\",\"father\"]},\"newvalue\": null,\"salary\": 3.22, \"boolvalue\": false"});
		bret = json.parseString("{\"person\" :{ \"age\" :13 , \"relations\":[\"mother\",\"father\"]},\"newvalue\": null,\"salary\": 3.22, \"boolvalue\": false}");
		assertEquals("parse ok",bret,true);
		obj = json.getObject("/person");
		json.Clone(obj);
		keys = json.getKeys("/");
		assertEquals(String.format("person keys length"),keys.length,2);
	}	

	@Test
	public void test_A008() throws JsonExtNotParsedException, JsonExtNotFoundException, JsonExtInvalidTypeException {
		JsonExt jsonext = new JsonExt();
		Object aobj;
		boolean bret;
		bret = jsonext.parseString("{\"array\" : []}");
		assertEquals(String.format("parse array"),bret,true);
		aobj = jsonext.getObject("/array");
		return ;
	}
}