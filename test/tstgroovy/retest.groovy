@Grapes([
  @Grab(group="net.sourceforge.argparse4j", module="argparse4j", version="0.7.0")
])

import static net.sourceforge.argparse4j.impl.Arguments.storeTrue
import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.inf.Subparser
import net.sourceforge.argparse4j.inf.ArgumentParserException

def parser = ArgumentParsers.newArgumentParser("retest")
			.defaultHelp(True)

subparser = parser.addSubParsers()
				.title("subcommand")
				.metavar("subcommand")
				.help("regular expression handler")

parser.addArgument('-i','--input')
	.dest('input')
	.setDefault(null)
	.help('input file specified')

findparser = subparser.addParser('findall')
				.help('re.findall handle')

try{
	ns = parser.parseArgs()

	} catch(ArgumentParserException e){

	}