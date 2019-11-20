package pie.core.engine;


import pie.core.engine.worker.IWorker;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public interface IFutureTaskListener {
	
	public void done(IWorker worker);
}
