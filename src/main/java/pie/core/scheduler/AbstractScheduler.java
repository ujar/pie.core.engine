package pie.core.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 2.0
 */
public abstract class AbstractScheduler implements IScheduler {

    private boolean schedule = false;
    private long wakeupDelay = 2;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public static enum SCHEDULE {

        MINUTE(1),
        HOURLY(1),
        DAILY(1),
        WEEKLY(7),
        MONTHLY(30);
        private final int value;

        SCHEDULE(int value) {
            this.value = value;
        }

        public int toValue() {
            return this.value;
        }

        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public abstract SCHEDULE getScheduleRate();

    public abstract Calendar getFutureDate();

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public void setWakeupDelay(long wakeupDelay) {
        this.wakeupDelay = wakeupDelay;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Override
    public long scheduleTime() {
        long scheduleTimeInSecond = (getFutureDate().getTimeInMillis() - System.currentTimeMillis()) / 1000;
        return scheduleTimeInSecond;
    }

    @Override
    public boolean schedule() {
        return this.schedule;
    }

    @Override
    public long wakeupDelay() {
        if (schedule()) {
            Calendar nextDateOfFutureDate = getNext(getFutureDate());
            long delay = nextDateOfFutureDate.getTimeInMillis() - getFutureDate().getTimeInMillis();
            return delay / 1000;
        }
        return this.wakeupDelay;//this delay unit will be counted as delayTimeUnit
    }

    @Override
    public TimeUnit delayTimeUnit() {
        if (schedule()) {
            return TimeUnit.SECONDS;
        }
        return this.timeUnit;
    }

    public Calendar getNext(Calendar calendar) {
        SCHEDULE schedule = getScheduleRate();
        //create new one and put all the value from old calender to this new
        //calender .manual clone
        Calendar nextCalender = new GregorianCalendar();
        nextCalender.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK));
        nextCalender.set(Calendar.HOUR, calendar.get(Calendar.HOUR));
        nextCalender.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        nextCalender.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
        nextCalender.set(Calendar.AM_PM, calendar.get(Calendar.AM_PM));

        //now add day/week/hour in this calender
        if (schedule == SCHEDULE.DAILY) {
            nextCalender.add(Calendar.DATE, schedule.DAILY.toValue());
        } else if (schedule == SCHEDULE.WEEKLY) {
            nextCalender.add(Calendar.DATE, schedule.WEEKLY.toValue());
        } else if (schedule == SCHEDULE.HOURLY) {
            nextCalender.add(Calendar.HOUR, schedule.HOURLY.toValue());
        } else {
            //do nothing
        }
        return nextCalender;
    }

    public Date getTomorrow(int hour, int minute) {
        Calendar today = new GregorianCalendar();
        today.add(Calendar.DATE, 1);
        Calendar tomorrow = new GregorianCalendar(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DATE),
                hour,
                minute);

        return tomorrow.getTime();
    }

    public static Date getToday(int hour, int minute) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        return calendar.getTime();
    }
}
