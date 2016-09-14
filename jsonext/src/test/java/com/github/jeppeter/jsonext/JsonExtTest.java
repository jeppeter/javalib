package com.github.jeppeter.jsonext;

import com.github.jeppeter.jsonext.JsonExtInvalidTypeException;
import com.github.jeppeter.jsonext.JsonExtNotFoundException;
import com.github.jeppeter.jsonext.JsonExtNotParsedException;
import com.github.jeppeter.jsonext.JsonExt;

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


}