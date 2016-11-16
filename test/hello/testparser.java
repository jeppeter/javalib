
import com.github.jeppeter.extargsparse4j.Parser;
import com.github.jeppeter.extargsparse4j.NameSpaceEx;

public class testparser {
	public static void main(String[] params) throws Exception {
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
        parser = new Parser();
        parser.load_command_line_string(loads);
       	args = parser.parse_command_line(params);
        System.out.println(args.toString());
        return;
	}
}