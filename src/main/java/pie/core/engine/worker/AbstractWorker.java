package pie.core.engine.worker;

import pie.core.engine.Event;
import java.util.List;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public abstract class AbstractWorker implements IWorker<Event> {

	@Override
	public List<Event> call() throws Exception {
		return doWork();
	}
	

	
}
