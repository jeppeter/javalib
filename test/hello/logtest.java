import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class logtest {
	public static void main(String[] args)  {
		Logger logger = LogManager.getRootLogger();
		logger.debug("debug info");
		logger.info("info ");
		logger.warn("warn ");
		logger.error("error ");
		logger.fatal("fatal "); 
	}
}