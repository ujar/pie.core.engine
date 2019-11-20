package pie.core.adapter;

import pie.core.engine.Event;
import pie.core.scheduler.AbstractScheduler;
import pie.core.scheduler.IScheduler;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 * @
 */
public abstract class AbstractWorkerAdapter extends AbstractScheduler implements IWorkerAdapter {

    private boolean schedule = false;
    private Date scheduleDate = null;
    private SCHEDULE scheduleRate;
    private long wakeupDelay;
    private TimeUnit timeUnit;

    public boolean schedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public void setScheduleRate(SCHEDULE scheduleRate) {
        this.scheduleRate = scheduleRate;
    }

    public <T extends IScheduler> T getId() {
        IWorkerAdapter iWorkerAdapter = getWorkerId();
        return (T) iWorkerAdapter;
    }

    @Override
    public Calendar getFutureDate() {
        Calendar futureCalendar = new GregorianCalendar();
        futureCalendar.setTime(this.scheduleDate);
        return futureCalendar;
    }

    @Override
    public SCHEDULE getScheduleRate() {
        return scheduleRate;
    }

    public TimeUnit getTimeUnit(){
        return this.timeUnit;
    }
    
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
    
    public Long getWakeupDelay() {
        return this.wakeupDelay;
    }
    
    public void setWakeupDelay(long wakeupDelay) {
        this.wakeupDelay = wakeupDelay;
    }

    
    
    @Override
    public long wakeupDelay() {
        if(schedule()){
            return super.wakeupDelay();
        }
        return this.wakeupDelay;
    }

    @Override
    public TimeUnit delayTimeUnit() {
        return this.timeUnit;
    }

    public List<Event> process() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
