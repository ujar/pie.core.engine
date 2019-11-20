package pie.core.engine.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class IsoapLogger {

	private Class clazz = null;
	private Log LOGGER = null;
	
	private IsoapLogger(Class clazz){
		this.clazz = clazz;
		LOGGER = LogFactory.getLog(clazz);
	}
	
	public static IsoapLogger getLogger(Class clazz){
		return new IsoapLogger(clazz);
	}
	
	public void info(Object obj){
            System.out.println(obj.toString());
		LOGGER.info(obj);
	}
}
