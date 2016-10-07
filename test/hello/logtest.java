import org.apache.log4j.Logger;

public class logtest {
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(logtest.class);
		logger.debug("debug info");
		logger.info("info ");
		logger.warn("warn ");
		logger.error("error ");
		logger.fatal("fatal "); 
	}
}