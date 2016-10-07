import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import enumval.logc;

public class logtest {
	public static void main(String[] args)  {
		Logger logger = LogManager.getRootLogger();
		logc lg = new logc();
		logger.debug("debug info");
		logger.info("info ");
		logger.warn("warn ");
		logger.error("error ");
		logger.fatal("fatal "); 

		lg.debug();
		return;
	}
}