package enumval;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



public class logc {
	final static Logger m_logger = LogManager.getLogger(logc.class.getName());

	public logc() {
	}

	public void debug() {
		m_logger.debug("debug info");
		m_logger.info("info ");
		m_logger.warn("warn ");
		m_logger.error("error ");
		m_logger.fatal("fatal "); 
		return;
	}
}