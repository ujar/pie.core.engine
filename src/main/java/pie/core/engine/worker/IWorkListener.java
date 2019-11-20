package pie.core.engine.worker;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public interface IWorkListener {

	public void finished(IWorker worker);
	public void workerAdapterIsShutdown(IWorker worker);
        public void workerAdapterIsStarted(IWorker worker);
	public void workIsProcessing(IWorker worker);
        public String getSourceName();

}
