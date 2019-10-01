package com.isoap.core.engine;

import com.isoap.core.engine.worker.IWorkListener;
import com.isoap.core.engine.worker.IWorker;
import java.util.Set;


/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public interface IEngine {
	/**
	 * 
	 * @param configuration
	 */
	public void start(IConfiguration configuration);
	/**
	 * 
	 */
	public void stop();
	/**
	 *  This method is for adding new tasks in the core engine
	 * @param worker
	 * @param taskFinishListener
	 */
	public void runWork(IWorker<Event> worker,IWorkListener... workListener);
        
        public void unRegisterListener(IWorker<Event> worker);
	
	/**
	 * 
	 * @param engineListener
	 */
	public void addEngineListener(IEngineListener... engineListener);
	/**
	 * 
	 * @param worker
	 * @param workListeners
	 */
        @Deprecated
	public void addWorkListenerOnTheFly(IWorker<Event> worker,IWorkListener... workListeners);
	
	public boolean isRunning();
	
	public void cancel(IWorker<Event> worker);
	
	public void sendMessageToGroup(String groupName,String message);
	
	public boolean isShutdown();
	
        
        public Set<Class<IWorker<Event>>> getListOfRunningAdaptersId();
        
        
        public void addWorkListenerOnTheFly(Class id,IWorkListener... workListeners);
        
        public boolean isStop(IWorker<Event> worker);
        public boolean isStop(Class adapterId);
	
	
	
}
