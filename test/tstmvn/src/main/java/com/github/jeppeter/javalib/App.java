package com.github.jeppeter.javalib;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	ArgumentParser parser = ArgumentParsers.newArgumentParser("loguse")
    	            .defaultHelp(true)
    	            .description("log dependend");
    	parser.AddArgument('-l','--log')
    }
}
