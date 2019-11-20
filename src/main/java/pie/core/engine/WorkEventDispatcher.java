package pie.core.engine;

import pie.core.engine.Event;
import pie.core.engine.worker.IWorkListener;
import pie.core.engine.worker.IWorker;
import java.util.List;
import java.util.Map;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class WorkEventDispatcher {

	
	WorkEventDispatcher(){}
	
	
	 void notifyWorkIsDone(IWorker worker,Map<Class<IWorker<Event>>,List<IWorkListener>> listenerMap){
		 for(IWorkListener listener : listenerMap.get(worker.getId())){
			 listener.finished(worker);
		 }
	}

	
	 void notifyWorkIsShutDown(IWorker worker,Map<Class<IWorker<Event>>,List<IWorkListener>> listenerMap){
		 for(IWorkListener listener : listenerMap.get(worker.getId())){
			 listener.workerAdapterIsShutdown(worker);
		 }
	}

	 void notifyWorkIsProcessing(IWorker worker,Map<Class<IWorker<Event>>,List<IWorkListener>> listenerMap){
		 for(IWorkListener listener : listenerMap.get(worker.getId())){
			 listener.workIsProcessing(worker);
		 }
	}

         void notifyWorkIsStarted(IWorker worker,Map<Class<IWorker<Event>>,List<IWorkListener>> listenerMap){
		 for(IWorkListener listener : listenerMap.get(worker.getId())){
			 listener.workerAdapterIsStarted(worker);
		 }
	}
         
          
	
}
