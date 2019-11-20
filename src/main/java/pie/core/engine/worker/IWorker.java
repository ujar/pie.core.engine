package pie.core.engine.worker;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public interface IWorker<Event> extends Callable<List<Event>> {

	
	public  Class getId();
	public  long wakeupDelay();
	public TimeUnit delayTimeUnit();
        public long getScheduleTime();//in milisecond.
        public boolean isScheduleJob();
	public abstract List<Event> doWork();
	public  boolean isDaemon();
	public void setResult(List<Event> events);
	public List<Event> getResult();
	 
}
