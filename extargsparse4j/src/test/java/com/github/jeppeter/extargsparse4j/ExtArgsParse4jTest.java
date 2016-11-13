package com.github.jeppeter.extargsparse4j;

//import java.util.regex.*;
import com.github.jeppeter.extargsparse4j.ParserException;
import com.github.jeppeter.extargsparse4j.Parser;
import com.github.jeppeter.extargsparse4j.NameSpaceEx;

import static org.junit.Assert.assertEquals;
import org.junit.*;

public class ExtArgsParse4jTest {
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
	    parser = new Parser();
	    parser.load_command_line_string(loads);
	    args = parser.parse_command_line(params);
		return;
	}
}