package com.github.jeppeter.extargsparse4j;

//import java.util.regex.*;
import com.github.jeppeter.extargsparse4j.KeyException;
import com.github.jeppeter.extargsparse4j.Key;
//import org.json.simple.JSONObject;

import static org.junit.Assert.assertEquals;
import org.junit.*;

public class KeyTest {
	@Test
	public void test_A001() {
		Key flags = Key("","$flag|f+type","string",False);
		assertEquals(String.format("(%s)flagname",flags.get_string_value("origkey")),flags.get_string_value("flagname"),"flag");
		assertEquals(String.format("(%s)longopt",flags.get_string_value("longopt")),flags.get_string_value("longopt"),"--type-flag");
	} 
}