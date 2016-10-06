package com.github.jeppeter.extargsparse4j;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import com.github.jeppeter.extargsparse4j.Priority;

public class Parser  {
	private ArgumentParser m_parser;
	public Parser(Priority[] priority,String description,String caption,Boolean defaulthelp) {

	}

	public Parser(Priority[] priority,String description,String caption) {
		this(priority,description,caption,true);
	}

	public Parser(Priority[] priority,String description) {
		/*now to get from the main*/ 
	}
}