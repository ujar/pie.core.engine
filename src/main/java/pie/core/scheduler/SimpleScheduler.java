package pie.core.scheduler;

import pie.core.engine.Event;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 2.0
 */
public class SimpleScheduler extends AbstractScheduler {

    public Calendar scheduleDate = null;
    private SCHEDULE schedule = null;
    private IJobSchedule iJobSchedule = null;

    public SimpleScheduler(Calendar scheduleDate, SCHEDULE schedule) {
        this.setSchedule(true);
        this.scheduleDate = scheduleDate;
        this.schedule = schedule;
    }

    public SimpleScheduler(long wakeupDelay, TimeUnit timeUnit) {
        this.setSchedule(false);
        this.setWakeupDelay(wakeupDelay);
        this.setTimeUnit(timeUnit);
    }

    
    public SimpleScheduler(long wakeupDelay, TimeUnit timeUnit,IJobSchedule iJobSchedule) {
        this.setSchedule(false);
        this.setWakeupDelay(wakeupDelay);
        this.setTimeUnit(timeUnit);
    }
    
    public SimpleScheduler(boolean isScheduledJob, long wakeupDelay, TimeUnit timeUnit) {
        this.setSchedule(isScheduledJob);
        this.setWakeupDelay(wakeupDelay);
        this.setTimeUnit(timeUnit);
    }
    
    public <T extends IScheduler> T getId() {
        return (T) this;
    }

    @Override
    public Calendar getFutureDate() {
        return this.scheduleDate;
    }

    @Override
    public SCHEDULE getScheduleRate() {
        return this.schedule;
    }

    public List<Event> process() {

       
        return null;
    }
}
