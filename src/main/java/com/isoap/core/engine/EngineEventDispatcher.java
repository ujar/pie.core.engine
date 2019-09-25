package com.isoap.core.engine;

import java.util.List;
/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class EngineEventDispatcher {

	
	EngineEventDispatcher(){}
	
	 void notifyEngineStopRequested(List<IEngineListener> engineListener){
		 for(IEngineListener listener : engineListener){
			 listener.engineStopRequestd();
		 }
	 }
	 
	  void notifyEngineStopping(List<IEngineListener> engineListener){
		 for(IEngineListener listener : engineListener){
			 listener.engineStopping();
		 }
	 }
	 
	  void notifyEngineStopped(List<IEngineListener> engineListener){
		 for(IEngineListener listener : engineListener){
			 listener.engineStopped();
		 }
	 }
	 
	  void notifyEngineStarted(List<IEngineListener> engineListener){
		 for(IEngineListener listener : engineListener){
			 listener.engineStarted();
		 }
	 }

          void notifyAnonymousEvent(List<IEngineListener> engineListener,String eventMessage){
		 for(IEngineListener listener : engineListener){
			 listener.anonymousEvent(eventMessage);
		 }
	 }
}
