package com.github.jeppeter.jsonext;

import com.github.jeppeter.jsonext.JsonExtInvalidTypeException;
import com.github.jeppeter.jsonext.JsonExtNotFoundException;
import com.github.jeppeter.jsonext.JsonExtNotParsedException;
import com.github.jeppeter.jsonext.JsonExt;

import static org.junit.Assert.assertEquals;
import org.junit.*;


public class JsonExtTest {
	@Test
	public void test_A001() {
		JsonExt json = new JsonExt();
		boolean bret;
		String value="";

		bret = json.parseString("{\"name\" : \"jack\"}");
		assertEquals("parse ok",bret,true);
		bret = true;
		try{
			value = json.getString("name");
		}
		catch(JsonExtNotParsedException e) {
			bret = false;
		}
		catch(JsonExtInvalidTypeException e) {
			bret = false;
		}
		catch(JsonExtNotFoundException e) {
			bret = false;
		}
		assertEquals("get name ok",bret,true);
		assertEquals("get name (jack)","jack",value);
		return;
	}

	@Test
	public void test_A002() {
		JsonExt json = new JsonExt();
		boolean bret;
		String value="";
		String key="person/name";

		bret = json.parseString("{\"person\" :{ \"name\" :\"jack\"}}");
		assertEquals("parse ok",bret,true);
		bret = true;
		try{
			value = json.getString(key);
		}
		catch(JsonExtNotParsedException e) {
			bret = false;
		}
		catch(JsonExtInvalidTypeException e) {
			bret = false;
		}
		catch(JsonExtNotFoundException e) {
			bret = false;
		}
		assertEquals(String.format("get %s ok",key),bret,true);
		assertEquals(String.format("get %s (jack)",key),"jack",value);
		return;
	}

	@Test
	public void test_A003() {
		JsonExt json = new JsonExt();
		boolean bret;
		Long value=new Long(0);
		String key="person/age";

		bret = json.parseString("{\"person\" :{ \"age\" :13}}");
		assertEquals("parse ok",bret,true);
		bret = true;
		try{
			value = json.getLong(key);
		}
		catch(JsonExtNotParsedException e) {
			bret = false;
			System.err.println(e);
		}
		catch(JsonExtInvalidTypeException e) {
			bret = false;
			System.err.println(e);
		}
		catch(JsonExtNotFoundException e) {
			bret = false;
			System.err.println(e);
		}
		assertEquals(String.format("get %s ok",key),bret,true);
		assertEquals(String.format("get %s (13)",key),new Long(13),value);
		return;
	}


}