package com.isoap.core.scheduler;

import com.isoap.core.engine.Event;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 2.0
 */
public interface IScheduler {
    
    public  <T extends IScheduler> T getId();
    public  long wakeupDelay();
    public long scheduleTime();
    public TimeUnit delayTimeUnit();
    public List<Event> process();
    public boolean schedule();
}
