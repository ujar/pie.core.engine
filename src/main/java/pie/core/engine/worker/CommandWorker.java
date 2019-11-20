package pie.core.engine.worker;

import pie.core.scheduler.IScheduler;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pie.core.adapter.IWorkerAdapter;
import pie.core.engine.DirtyEvent;
import pie.core.engine.Event;
import pie.core.scheduler.AbstractScheduler;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class CommandWorker extends AbstractWorker {
    
    private static Log LOGGER = LogFactory.getLog(CommandWorker.class);
    private String name = "";
    private IScheduler scheduleWorker = null;
    private COMMAND[] commands;
    private boolean isDaemon = false;
    private List<Event> events = null;
    private ICommandWorker iCommandWorker = null;
    private String identifier = "";

    
    
    public CommandWorker(String name) {
        this.name = name;
    }
    
    public CommandWorker(String name, boolean isDaemon, IScheduler scheduleWorker, COMMAND... commands) {
        this.name = name;
        this.scheduleWorker = scheduleWorker;
        this.commands = commands;
        this.isDaemon = isDaemon;
    }
    
    
    public CommandWorker(String name, boolean isDaemon, IScheduler scheduleWorker, ICommandWorker iCommandWorker, COMMAND... commands) {
        this.name = name;
        this.scheduleWorker = scheduleWorker;
        this.commands = commands;
        this.isDaemon = isDaemon;
        this.iCommandWorker = iCommandWorker;
    }
    
    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    @Override
    public Class getId() {
        return scheduleWorker.getId().getClass();
    }
    
    @Override
    public List<Event> doWork() {
        System.out.println("Work has started to process");
        
        
        
        List<Event> events = null;
        try {
            if (scheduleWorker instanceof IWorkerAdapter) {
                 System.out.println("Work has started to process: " + 1);
                events = processWorkerAdapter();
            } else {
                 System.out.println("Work has started to process: " + 2);
                events = processScheduleService();
            }
        } catch (Throwable e) {
            LOGGER.error("DirtyEvent: " + getId() + " : "  + e);
            DirtyEvent dirtyEvent = new DirtyEvent("DirtyEvent", e.getMessage(), e,e.getMessage());
            events = new LinkedList<Event>();
            events.add(dirtyEvent);
        }
         System.out.println("Work has ended to process");
        return events;
    }
    
    @Override
    public boolean isDaemon() {
        return isDaemon;
    }
    
    @Override
    public long wakeupDelay() {
        try {
            if (this.scheduleWorker.schedule()) {
                AbstractScheduler abstractScheduler = ((AbstractScheduler) this.scheduleWorker);
                return abstractScheduler.wakeupDelay();
            } else {
                return this.scheduleWorker.wakeupDelay();
            }
        } catch (Throwable e) {
            e.printStackTrace();;
        }
        return 0;
    }
    
    @Override
    public TimeUnit delayTimeUnit() {
        return this.scheduleWorker.delayTimeUnit();
    }
    
    @Override
    public List<Event> getResult() {
        return this.events;
    }
    
    @Override
    public void setResult(List<Event> events) {
        this.events = events;
    }
    
    private List<Event> processWorkerAdapter() {
        IWorkerAdapter workerAdapter = (IWorkerAdapter) this.scheduleWorker;
        List<Event> events = null;
        for (COMMAND command : commands) {
            if (command == COMMAND.LOAD_FROM_PERSISTANT) {
                 System.out.println("Work has started to process: " + 3);
                events = workerAdapter.loadFromPersistant();
            } else if (command == COMMAND.LOAD_REALTIME) {
                 System.out.println("Work has started to process: " + 4);
                events = workerAdapter.loadRealTime();
            } else if (command == COMMAND.LOAD_REAL_TIME_AND_SAVE) {
                 System.out.println("Work has started to process: " + 5);
                events = workerAdapter.loadRealTime();
                events = workerAdapter.save(events);
            }
        }
        return events;
    }
    
    private List<Event> processScheduleService() {
        List<Event> events = null;
        
        if(this.iCommandWorker != null){
            iCommandWorker.processWork(this.identifier);
        return events;    
        }
        for (COMMAND command : commands) {
            if (command == COMMAND.SCHEDULE_OPERATION) {
                events = this.scheduleWorker.process();
                break;
            }
        }
        return events;
    }
    
    public long getScheduleTime() {
        return this.scheduleWorker.scheduleTime();
    }
    
    public boolean isScheduleJob() {
        return this.scheduleWorker.schedule();
    }
}
