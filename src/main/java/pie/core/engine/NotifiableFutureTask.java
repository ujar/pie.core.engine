package pie.core.engine;

import pie.core.engine.IFutureTaskListener;
import pie.core.engine.validation.IsoapExcetion;
import pie.core.engine.worker.IWorker;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class NotifiableFutureTask<Event> extends FutureTask<List<Event>>{

	private IWorker<Event> worker = null;
	private IFutureTaskListener futureTaskListener = null;
	
	public NotifiableFutureTask(IWorker<Event> worker) {
		super(worker);
		this.worker = worker;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return super.cancel(mayInterruptIfRunning);
	}

	@Override
	protected void done() {
		taskFinished();
	}

	 
	
	@Override
	public boolean isCancelled() {
		return super.isCancelled();
	}

	
	public void addTaskFinishListener(IFutureTaskListener futureTaskListener){
		this.futureTaskListener = futureTaskListener;
	}
	
	public void taskFinished(){
		try {
			worker.setResult(this.get());
		} catch (InterruptedException e) {
			throw new IsoapExcetion(e);
		} catch (ExecutionException e) {
			throw new IsoapExcetion(e);
		}
		futureTaskListener.done(worker);
	}

    @Override
    protected void setException(Throwable t) {
         t.printStackTrace();
        super.setException(t);
    }
        
        
}
