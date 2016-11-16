package com.github.jeppeter.extargsparse4j;

//import java.util.regex.*;
import com.github.jeppeter.extargsparse4j.ParserException;
import com.github.jeppeter.extargsparse4j.Parser;
import com.github.jeppeter.extargsparse4j.NameSpaceEx;

import com.github.jeppeter.jsonext.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import static org.junit.Assert.assertEquals;
import org.junit.*;

public class ParserTest {
	private Logger m_logger;

	@Before
	public void Setup() {
		this.__get_logger();
	}


	private void __get_logger() {
		if (this.m_logger == null) {
			this.m_logger = LogManager.getLogger(this.getClass().getName());
		}
	}

	private void assert_string_value(NameSpaceEx args,String key,String value) {
		assertEquals(String.format("%s=%s",key,value),(String)args.get(key),value);
		return;
	}

	private void assert_int_value(NameSpaceEx args,String key,Integer value) {
		//this.m_logger.info(String.format("%s=%s %s",key,value.toString(),args.get(key).toString()));
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
	          + "\"list|l\" : [],\n"
	          + "\"string|s\" : \"string_var\",\n"
	          + "\"$\" : {\n"
	          +     "\"value\" : [],\n"
	          +     "\"nargs\" : \"*\",\n"
	          +     "\"type\" : \"string\"\n"
	          + "},\n"
	          + "\"number|n\" : 0\n"
	        + "}\n";
	    Parser parser ;
	    NameSpaceEx args;
	    String[] params = {"-vvvv","-f","-n","30","-l","bar1","-l","bar2","var1","var2"};
	    Object dobj;
	    JsonExt jext = new JsonExt();
	    parser = new Parser();
	    parser.load_command_line_string(loads);
	    args = parser.parse_command_line(params);
	    this.assert_int_value(args,"verbose",new Integer(4));
	    this.assert_bool_value(args,"flag",new Boolean(true));
	    this.assert_int_value(args,"number",new Integer(30));
	    jext.parseString("{ \"dummy\": [\"bar1\",\"bar2\"]}");
	    dobj = jext.getObject("/dummy");
	    this.assert_object_value(args,"list",dobj);
	    jext.parseString("{ \"dummy\": [\"var1\",\"var2\"]}");
	    dobj = jext.getObject("/dummy");
	    this.assert_object_value(args,"args",dobj);
		return;
	}

	@Test
	public void test_A002() throws Exception {
		String loads = "{"
				+ " \"verbose|v\" : \"+\",\n"
            	+ "     \"dep\" : {\n"
                + "          \"list|l\" : [],\n"
                + "          \"string|s\" : \"s_var\",\n"
                + "          \"$\" : \"+\"\n"
                + "          \"master|m\" : [],\n"
            	+ "    },"
            	+ "\"port|p\" : 3000\n"
            	+ "}";
        Parser parser;
        NameSpaceEx args;
	    Object dobj;
	    JsonExt jext = new JsonExt();
	    String[] params = {"-vvvv","-p","5000","dep","--dep-list","arg1","--dep-list","arg2","cc","dd"};
	    String debugstr="";
        parser = new Parser();
        parser.load_command_line_string(loads);
        for (String c : params) {
        	if (debugstr.length() > 0) {
        		debugstr += " ";
        	}
        	debugstr += c;
        }
        //this.m_logger.info(String.format("params (%s)",debugstr));
        args = parser.parse_command_line(params);
	    this.assert_int_value(args,"verbose",new Integer(4));
	    this.assert_int_value(args,"port",new Integer(5000));
	    this.assert_string_value(args,"subcommand","dep");
	    jext.parseString("{ \"dummy\": [\"arg1\",\"arg2\"]}");
	    dobj = jext.getObject("/dummy");
	    this.assert_object_value(args,"dep_list",dobj);
	    this.assert_string_value(args,"dep_string","s_var");
	    jext = new JsonExt();
	    jext.parseString("{ \"dummy\": [\"cc\",\"dd\"]}");
	    dobj = jext.getObject("/dummy");
	    this.assert_object_value(args,"subnargs",dobj);
	    return;
	}

	@Test
	public void test_A003() throws Exception {
		String loads = "{"
            + "\"verbose|v\" : \"+\",\n"
            + "\"port|p\" : 3000,\n"
            + "\"dep\" : {\n"
            + "       \"list|l\" : [],\n"
            + "       \"string|s\" : \"s_var\",\n"
            + "       \"$\" : \"+\"\n"
            + "},\n"
            + "\"rdep\" : {\n"
            + "       \"list|L\" : [],\n"
            + "       \"string|S\" : \"s_rdep\",\n"
            + "       \"$\" : 2\n"
            + "}\n"
            + "}\n";
        Parser parser ;
        String[] params = {"-vvvv","-p","5000","rdep","-L","arg1","--rdep-list","arg2","cc","dd"};
        NameSpaceEx args;
	    Object dobj;
	    JsonExt jext = new JsonExt();
        parser = new Parser();
        parser.load_command_line_string(loads);
        args = parser.parse_command_line(params);
        this.assert_int_value(args,"verbose",new Integer(4));
        this.assert_int_value(args,"port",new Integer(5000));
        this.assert_string_value(args,"subcommand","rdep");
		jext.parseString("{\"dummy\": [\"arg1\",\"arg2\"]}");
		dobj = jext.getObject("/dummy");
		this.assert_object_value(args,"rdep_list",dobj);
		this.assert_string_value(args,"rdep_string","s_rdep");
		jext.parseString("{\"dummy\": [\"cc\",\"dd\"]}");
		dobj = jext.getObject("/dummy");
		this.assert_object_value(args,"subnargs",dobj);
		return;
	}
}