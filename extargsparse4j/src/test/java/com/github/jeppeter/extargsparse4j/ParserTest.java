package com.github.jeppeter.extargsparse4j;

//import java.util.regex.*;
import com.github.jeppeter.extargsparse4j.ParserException;
import com.github.jeppeter.extargsparse4j.Parser;
import com.github.jeppeter.extargsparse4j.NameSpaceEx;

import static org.junit.Assert.assertEquals;
import org.junit.*;

public class ParserTest {

	private void assert_string_value(NameSpaceEx args,String key,String value) {
		assertEquals(String.format("%s=%s",key,value),(String)args.get(key),value);
		return;
	}

	private void assert_int_value(NameSpaceEx args,String key,Integer value) {
		assertEquals(String.format("%s=%d",key,value),(Integer)args.get(key),value);
		return;
	}

	private void assert_bool_value(NameSpaceEx args,String key, Boolean value) {
		assertEquals(String.format("%s=%s",key,value.toString()),(Boolean)args.get(key),value);
		return;
	}

	private void assert_object_value(NameSpaceEx args,String key,Object value) {
		assertEquals(String.format("%s=%s",key,value.toString()),args.get(key),value);
		return;
	}

	@Test
	public void test_A001() throws Exception {
		String loads = "{"
	          + "\"verbose|v##increment verbose mode##\" : \"+\",\n"
	          + "\"flag|f## flag set##\" : false,\n"
	          + "\"number|n\" : 0,\n"
	          + "\"list|l\" : [],\n"
	          + "\"string|s\" : \"string_var\",\n"
	          + "\"$\" : {\n"
	          +     "\"value\" : [],\n"
	          +     "\"nargs\" : \"*\",\n"
	          +     "\"type\" : \"string\"\n"
	          + "}"
	        + "}";
	    Parser parser ;
	    NameSpaceEx args;
	    String[] params = {"-vvvv","-f","-n","30","-l","bar1","-l","bar2","var1","var2"};
	    Object dobj;
	    parser = new Parser();
	    parser.load_command_line_string(loads);
	    args = parser.parse_command_line(params);
	    this.assert_int_value(args,"verbose",Integer(4));
	    this.assert_bool_value(args,"flag",Boolean(true));
	    this.assert_int_value(args,"number",Integer(30));
	    this.assert_object_value(args,"list",)
		return;
	}
}