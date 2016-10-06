package com.github.jeppeter.extargsparse4j;

//import java.util.regex.*;
import com.github.jeppeter.extargsparse4j.KeyException;
import com.github.jeppeter.extargsparse4j.Key;
import com.github.jeppeter.jsonext.*;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;

import static org.junit.Assert.assertEquals;
import org.junit.*;

public class KeyTest {

	private void assert_string_value(Key flags, String keyname, String value)
	throws NoSuchFieldException, KeyException, IllegalAccessException {
		assertEquals(String.format("(%s)%s", flags.get_string_value("origkey"), keyname), flags.get_string_value(keyname), value);
		return;
	}

	private void assert_bool_value(Key flags, String keyname, Boolean bval)
	throws NoSuchFieldException, KeyException, IllegalAccessException {
		assertEquals(String.format("(%s)%s", flags.get_string_value("origkey"), keyname), flags.get_bool_value(keyname), bval);
		return;
	}

	private void assert_object_value(Key flags, String keyname, Object oval)
	throws NoSuchFieldException, KeyException, IllegalAccessException {
		assertEquals(String.format("(%s)%s", flags.get_string_value("origkey"), keyname), flags.get_object_value(keyname), oval);
		return;
	}

	@Test
	public void test_A001()  throws NoSuchFieldException, KeyException, IllegalAccessException,
		JsonExtInvalidTypeException, JsonExtNotParsedException, JsonExtNotFoundException {
		Key flags = new Key("", "$flag|f+type", "string", false);
		Object sobj = (Object) "string";
		this.assert_string_value(flags, "flagname", "flag");
		this.assert_string_value(flags, "longopt", "--type-flag");
		this.assert_string_value(flags, "shortopt", "-f");
		this.assert_string_value(flags, "optdest", "type_flag");
		this.assert_object_value(flags, "value", sobj);
		this.assert_string_value(flags, "type", "string");
		this.assert_string_value(flags, "shortflag", "f");
		this.assert_string_value(flags, "prefix", "type");
		this.assert_string_value(flags, "cmdname", null);
		this.assert_string_value(flags, "helpinfo", null);
		this.assert_string_value(flags, "function", null);
		this.assert_bool_value(flags, "isflag", true);
		this.assert_bool_value(flags, "iscmd", false);
		return;
	}

	@Test
	public void test_A002() throws NoSuchFieldException, KeyException, IllegalAccessException,
		JsonExtInvalidTypeException, JsonExtNotParsedException, JsonExtNotFoundException {
		JsonExt jsonext  = new JsonExt();
		Key flags ;
		Object aobj;

		jsonext.parseString("{\"array\" : []}");
		aobj = jsonext.getObject("/array");
		flags = new Key("", "$flag|f+type", aobj, true);
		this.assert_string_value(flags, "flagname", "flag");
		this.assert_string_value(flags, "shortflag", "f");
		this.assert_string_value(flags, "prefix", "type");
		this.assert_string_value(flags, "longopt", "--type-flag");
		this.assert_string_value(flags, "shortopt", "-f");
		this.assert_string_value(flags, "optdest", "type_flag");
		this.assert_string_value(flags, "helpinfo", null);
		this.assert_string_value(flags, "function", null);
		this.assert_string_value(flags, "cmdname", null);
		this.assert_bool_value(flags, "isflag", true);
		this.assert_bool_value(flags, "iscmd", false);
		return;
	}

	@Test
	public void test_A003() throws NoSuchFieldException, KeyException, IllegalAccessException,
		JsonExtInvalidTypeException, JsonExtNotParsedException, JsonExtNotFoundException {
		JsonExt jsonext = new JsonExt();
		Key flags;
		Object bobj;
		jsonext.parseString("{\"bool\" : false}");
		bobj = jsonext.getObject("/bool");
		flags = new Key("", "flag|f", bobj, false);
		this.assert_string_value(flags, "flagname", "flag");
		this.assert_string_value(flags, "shortflag", "f");
		this.assert_string_value(flags, "prefix", "");
		this.assert_string_value(flags, "longopt", "--flag");
		this.assert_string_value(flags, "shortopt", "-f");
		this.assert_string_value(flags, "optdest", "flag");
		this.assert_object_value(flags, "value", bobj);
		this.assert_string_value(flags, "type", "bool");
		this.assert_string_value(flags, "prefix", "");
		this.assert_string_value(flags, "helpinfo", null);
		this.assert_string_value(flags, "function", null);
		this.assert_string_value(flags, "cmdname", null);
		this.assert_bool_value(flags, "isflag", true);
		this.assert_bool_value(flags, "iscmd", false);
		return;
	}

}