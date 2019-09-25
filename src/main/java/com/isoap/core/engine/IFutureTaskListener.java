package com.isoap.core.engine;


import com.isoap.core.engine.worker.IWorker;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public interface IFutureTaskListener {
	
	public void done(IWorker worker);
}
