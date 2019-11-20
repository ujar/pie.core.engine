
package pie.core.scheduler;

import pie.core.engine.Event;
import java.util.List;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 2.0
 */
public interface IJobSchedule {
    public List<Event> process();
}
