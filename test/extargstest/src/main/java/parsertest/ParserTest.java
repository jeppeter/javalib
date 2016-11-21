import com.github.jeppeter.extargsparse4j.ParserException;
import com.github.jeppeter.extargsparse4j.Parser;
import com.github.jeppeter.extargsparse4j.NameSpaceEx;
import com.github.jeppeter.extargsparse4j.Environ;
import com.github.jeppeter.extargsparse4j.Priority;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;


public class ParserTest {
    public static void unset_environs(String[] envkey) {
        for (String c : envkey) {
            Environ.unsetenv(c);
        }
        return;
    }

    public static String write_temp_file(String pattern, String suffix, String content) {
        String retfilename = null;
        while (true) {
            retfilename = null;
            try {
                File temp = File.createTempFile(pattern, suffix);
                FileOutputStream outf;
                retfilename = temp.getAbsolutePath();
                outf = new FileOutputStream(retfilename);
                outf.write(content.getBytes());
                outf.close();
                return retfilename;
            } catch (IOException e) {
                ;
            }
        }
    }


    public static void main(String[] cmdargs) throws Exception {
        String commandline="{\n"
            + "    \"verbose|v\" : \"+\",\n"
            + "    \"$port|p\" : {\n"
            + "        \"value\" : 3000,\n"
            + "        \"type\" : \"int\",\n"
            + "        \"nargs\" : 1 , \n"
            + "        \"helpinfo\" : \"port to connect\"\n"
            + "    },\n"
            + "    \"dep\" : {\n"
            + "        \"list|l\" : [],\n"
            + "        \"string|s\" : \"s_var\",\n"
            + "        \"$\" : \"+\"\n"
            + "    }\n"
            + "}";
        Parser parser;
        String depjsonfile = null,jsonfile=null;
        String[] needenvs = {"EXTARGSPARSE_JSON", "DEP_JSON", "EXTARGS_VERBOSE", "EXTARGS_PORT", "DEP_LIST", "DEP_STRING"};
        String[] params = {"-p","9000","dep","--dep-string","ee","ww"};
        String depstrval,deplistval;
        NameSpaceEx args;
        Priority[] priority= {Priority.ENV_COMMAND_JSON_SET,Priority.ENVIRONMENT_SET,Priority.ENV_SUB_COMMAND_JSON_SET};
        int i;
        Object val;
        ParserTest.unset_environs(needenvs);

        try {
            depstrval = "newval";
            deplistval = "[\"depenv1\",\"depenv2\"]";
            jsonfile = ParserTest.write_temp_file("parse", ".json", "{\"dep\":{\"list\" : [\"jsonval1\",\"jsonval2\"],\"string\" : \"jsonstring\"},\"port\":6000,\"verbose\":3}\n");
            depjsonfile = ParserTest.write_temp_file("parse",".json","{\"list\":[\"depjson1\",\"depjson2\"]}\n");
            Environ.setenv("DEP_JSON",depjsonfile);
            Environ.setenv("EXTARGSPARSE_JSON",jsonfile);
            Environ.setenv("DEP_STRING",depstrval);
            Environ.setenv("DEP_LIST",deplistval);

            parser = new Parser(priority);
            parser.load_command_line_string(commandline);
            args = parser.parse_command_line(params);
            System.out.println(String.format("verbose %d",args.getLong("verbose")));
            System.out.println(String.format("port %d",args.getLong("port")));
            System.out.println(String.format("subcommand %s",args.getString("subcommand")));
            System.out.println(String.format("dep_list %s",args.get("dep_list").toString()));
            System.out.println(String.format("dep_string %s",args.getString("dep_string")));
            System.out.println(String.format("subnargs %s",args.get("subnargs").toString()));
        } finally {
            if (depjsonfile != null) {
                File file = new File(depjsonfile);
                file.delete();
                depjsonfile = null;
            }
            if (jsonfile != null) {
                File file = new File(jsonfile);
                file.delete();
                jsonfile = null;
            }
        }
        return;
    }
}
