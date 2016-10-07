package com.github.jeppeter.extargsparse4j;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import com.github.jeppeter.extargsparse4j.Priority;

import com.github.jeppeter.reext.ReExt;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class Parser  {
	private ArgumentParser m_parser;
	private Priority[] m_priorities; 
	private Logger m_logger;

	private static String get_main_class() {
		String command = System.getProperty("sun.java.command");		
		String[] names;
		String mainclass;
		names = ReExt.Split("\\s+",command);
		mainclass = "Parser";
		if (names.length > 0) {
			if (names[0].contains(".")) {
				names = ReExt.Split("\\.",names[0]);
				/*get last one*/
				mainclass = names[(names.length - 1)];
			} else {
				mainclass = names[0];
			}
		}
		return mainclass;		
	}

	public Parser(Priority[] priority,String caption,String description,Boolean defaulthelp) {
		Priority[] defpriority = {Priority.SUB_COMMAND_JSON_SET ,
		 Priority.COMMAND_JSON_SET ,Priority.ENVIRONMENT_SET,
		 Priority.ENV_SUB_COMMAND_JSON_SET , Priority.ENV_COMMAND_JSON_SET };

		 this.m_logger = LogManager.getLogger(this.class);
		 if (priority.length == 0 ) {
		 	this.m_priorities = defpriority;
		 } else {
		 	this.m_priorities = priority;
		 }

		 this.m_logger.info("priority (%s) m_priorities (%s) caption(%s) description (%s) help %s",
		 	priority,this.m_priorities,caption,description,defaulthelp ? "True" : "False");
		 this.m_parser = ArgumentParsers.newArgumentParser(caption)
		               .defaultHelp(defaulthelp)
		               .description(description);

	}

	public Parser(Priority[] priority,String caption,String description) {
		this(priority,caption,description,true);
	}

	public Parser(Priority[] priority,String caption) {
		/*now to get from the main class */ 
		String mainclass;
		String description;
		description = String.format("%s [OPTIONS] command ...",caption);
		this(priority,caption,description);
	}

	public Parser(Priority[] priority) {
		String mainclass;
		String description
		mainclass = get_main_class();
		this(priority,caption);
	}

	public Parser() {
		Priority[] priority = {};
		 this(priority);
	}
}