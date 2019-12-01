package pie.core.engine.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class PieLogger {

        private static boolean isConsole = true;
	private Class clazz = null;
	private static Log LOGGER = null;
	
	private PieLogger(Class clazz){
		this.clazz = clazz;
		LOGGER = LogFactory.getLog(clazz);
	}
	
	public static PieLogger getLogger(Class clazz){
		return new PieLogger(clazz);
	}
	
	public void info(Object obj){
            if(isConsole){
                System.out.println(obj.toString());
            } else {
                LOGGER.info(obj);
            }
   	}
        
        public static void log(Object obj){
            if(isConsole){
                System.out.println(obj.toString());
            } else {
                LOGGER.info(obj);
            }
   	}
        
}
