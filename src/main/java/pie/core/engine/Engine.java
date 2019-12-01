package pie.core.engine;

import pie.core.engine.security.Sms;
import pie.core.engine.util.IsoapLogger;
import pie.core.engine.validation.IsoapExcetion;
import pie.core.engine.validation.Guard;
import pie.core.engine.worker.IWorkListener;
import pie.core.engine.worker.IWorker;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class Engine implements IFutureTaskListener, IEngine {

    private static final IsoapLogger LOGGER = IsoapLogger.getLogger(Engine.class);
    private ScheduledExecutorService executor = null;
    private static boolean stopRequested = false;
    private ScheduledExecutorService wakerHandler = null;
    private List<IEngineListener> engineListener = new LinkedList<IEngineListener>();
    private IConfiguration configuration = null;
    private Map<Class<IWorker<Event>>, List<IWorkListener>> listenerMap = new LinkedHashMap<Class<IWorker<Event>>, List<IWorkListener>>();
    private List<IWorker<Event>> cancelWorkList = new LinkedList<IWorker<Event>>();
//	 private GeochatApi smsEngine = null;;
    private ScheduledFuture<?> coreWaker = null;
    private EngineEventDispatcher engineEventDispatcher = null;
    private WorkEventDispatcher workEventDispatcher = null;
    private Date engineStartedTime = null;
    private Date engineStoppedtime = null;
    private long endTime;
    private long startTime;
    private long stopRequestedTime;
    private long actualStopTime;
    private long usedTimeToStop;

    Engine() {
        engineEventDispatcher = new EngineEventDispatcher();
        workEventDispatcher = new WorkEventDispatcher();
    }

    //TODO before shutdown engine we should periodically shutdown all the adapter
    //and  then stop the core thread and whole process it should be synchronoused process
    private synchronized void stopEngine() {
        if (!isRunning()) {
            throw new IsoapExcetion("Engine is not running....Nothing to stop");
        }
        stopRequestedTime = System.currentTimeMillis();
        stopRequested = true;
        engineEventDispatcher.notifyEngineStopRequested(engineListener);
        LOGGER.info("Stop request is made");
    }

    private void stopCoreWalkerHandler() {
        LOGGER.info("Core waker is going to cancel: " + coreWaker.isCancelled());
        coreWaker.cancel(true);

        LOGGER.info("Core waker is Cancelled: " + coreWaker.isCancelled());
        LOGGER.info("Before cancel core waker is has done its processing : " + coreWaker.isDone());

        LOGGER.info("Going to shutdown waker handler");
        if (wakerHandler != null) {
            wakerHandler.shutdownNow();
        }

//                 wakerHandler.shutdown();
        LOGGER.info("Waker handler is shutdown now : " + wakerHandler.isShutdown());
        LOGGER.info("Waker handler is terminated now : " + wakerHandler.isTerminated());

        if (wakerHandler != null && wakerHandler.isShutdown()) {
            engineStoppedtime = new Date();
            engineEventDispatcher.notifyEngineStopped(engineListener);
            stopRequested = false;
            wakerHandler = null;
        }
    }

    private void startDeamonWaker() {
        final Runnable beeper = new Runnable() {

            public void run() {
//					LOGGER.info("Beams engine is running");
                //use while loop here which will check if the boolean flag is running or not
                if (stopRequested) {
                    LOGGER.info("Isoap Engine shutdown initiated");
                    engineEventDispatcher.notifyAnonymousEvent(engineListener, "Isoap Engine shutdown initiated");
                    if (isShutdown()) {
                        stopCoreWalkerHandler();
                    }
                    engineEventDispatcher.notifyEngineStopping(engineListener);
                    engineEventDispatcher.notifyAnonymousEvent(engineListener,
                            "Trying to cancel all pending task.no new adapter/tas  will be accepted");
                    LOGGER.info("Trying to cancel all pending task.no new task will be accepted");
                    executor.shutdown();
                }
                if (executor.isShutdown()) {
                    stopCoreWalkerHandler();
                    engineEventDispatcher.notifyEngineStopped(engineListener);
                    actualStopTime = System.currentTimeMillis();
                    usedTimeToStop = actualStopTime - stopRequestedTime;
                    engineEventDispatcher.notifyAnonymousEvent(engineListener, "Isoap Engine took "
                            + usedTimeToStop + " to stop!!");
                    LOGGER.info("Engine Stopped");
                }
//					if(!executor.isShutdown()){
//						//if not shut down this thread will sleep soon and its next wake this run
//						//method will invoke again
//						LOGGER.info("Engine is running");
//					} else {
//						stopCoreWalkerHandler();
//						LOGGER.info("Engine Stopped");
//					}
            }
        };

        wakerHandler = Executors.newScheduledThreadPool(1);
        coreWaker = wakerHandler.scheduleAtFixedRate(beeper, 1, 5, SECONDS);
    }

    private void startCoreWaker() {
        stopRequested = false;
        startDeamonWaker();
        engineStartedTime = new Date();
        endTime = System.currentTimeMillis();
        long usedTime = endTime - startTime;
        LOGGER.info("Isoap engine took  " + usedTime + " milli second to start");

        if (!executor.isShutdown()) {
            engineEventDispatcher.notifyEngineStarted(engineListener);
            engineEventDispatcher.notifyAnonymousEvent(engineListener, "Isoap engine took  "
                    + usedTime + " milli second to start");
        }
    }

    public void runWork(IWorker<Event> worker, IWorkListener... workListeners) {

        Guard.argumentNotNull(worker, "IWorker");
        Guard.argumentNotNull(workListeners, "IWorkListener");
        try {
            runInternal(worker, workListeners);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void runInternal(IWorker<Event> worker, IWorkListener... workListeners) {
        if (isRunning()) {
            if (listenerMap.containsKey(worker.getId())) {
                LOGGER.info("worker id " + worker.getId() + " is already served , and in que to be served\n"
                        + " you cant submit same adapter while its being working ");
                engineEventDispatcher.notifyAnonymousEvent(engineListener, "worker id "
                        + worker.getId() + " is already served , and in que to be served\n"
                        + " you cant submit same adapter while its being working ");
                return;
            }

            List<IWorkListener> wListeners = new LinkedList<IWorkListener>(Arrays.asList(workListeners));
            listenerMap.put(worker.getId(), wListeners);
            //System.out.println("................." + worker.getId());
            //System.out.println("id in the db " + worker.getId());
            NotifiableFutureTask<Event> notifiableFutureTask = createFutureTask(worker);
            notifiableFutureTask.addTaskFinishListener(this);

            if (worker.isScheduleJob()) {
                System.out.println("This is a scheduled job");
//                            executor.schedule(notifiableFutureTask,  worker.getScheduleTime(), worker.delayTimeUnit());
//                            executor.schedule(notifiableFutureTask,  worker.getScheduleTime(), TimeUnit.MILLISECONDS);
                executor.scheduleWithFixedDelay(notifiableFutureTask, worker.getScheduleTime(),
                        worker.wakeupDelay(), worker.delayTimeUnit());
            } else {
                System.out.println("This is not a scheduled job");
                executor.submit(notifiableFutureTask);
            }

            engineEventDispatcher.notifyAnonymousEvent(engineListener, "New Task / Adapter "
                    + worker.getId() + " has been submitted for processing");

            workEventDispatcher.notifyWorkIsStarted(worker, listenerMap);
            LOGGER.info("New Task " + worker.getId() + " has been submitted");
        } else {

            throw new IsoapExcetion("Engine needs to be started before running any task");
        }
    }

    /**
     * this method will add listener or observer dynamically to the specific
     * IWorker<Event> instance,instance which implemented by particular adapter
     */
    @Override
    @Deprecated
    public void addWorkListenerOnTheFly(IWorker<Event> worker, IWorkListener... workListeners) {
        Guard.argumentNotNull(worker, "IWorker");
        Guard.argumentNotNull(workListeners, "IWorkListener");

        for (IWorkListener listener : workListeners) {
            if (this.listenerMap.get(worker.getId()).contains(listener)) {
                LOGGER.info("One listener can not be added of more than one time");
                engineEventDispatcher.notifyAnonymousEvent(engineListener, listener.getSourceName()
                        + " alredy registered with with " + worker.getId()
                        + " it can not be added multiple times");
            } else {
                this.listenerMap.get(worker.getId()).add(listener);
            }
        }
    }

    public void addWorkListenerOnTheFly(Class id, IWorkListener... workListeners) {
        Guard.argumentNotNull(id, "id");
        Guard.argumentNotNull(workListeners, "IWorkListener");

        for (IWorkListener listener : workListeners) {
            if (this.listenerMap.get(id).contains(listener)) {
                LOGGER.info("One listener can not be added of more than one time to " + listener.getSourceName());
                engineEventDispatcher.notifyAnonymousEvent(engineListener, listener.getSourceName()
                        + " alredy registered with with " + id
                        + " it can not be added multiple times");
            } else {
                this.listenerMap.get(id).add(listener);
            }
        }
    }

    public Set<Class<IWorker<Event>>> getListOfRunningAdaptersId() {
        return listenerMap.keySet();
    }

    private NotifiableFutureTask<Event> createFutureTask(IWorker<Event> worker) {
        NotifiableFutureTask<Event> notifiableAdapterTask = new NotifiableFutureTask<Event>(worker);
        return notifiableAdapterTask;
    }

    @Override
    public void start(IConfiguration configuration) {
        Guard.argumentNotNull(configuration, "IConfiguration");

        if (isRunning()) {
            engineEventDispatcher.notifyAnonymousEvent(engineListener, "Engine already  started at : "
                    + engineStartedTime.toLocaleString());
            throw new IsoapExcetion("Engine has been started at : " + engineStartedTime.toLocaleString());
        }

        startTime = System.currentTimeMillis();
        this.configuration = configuration;
        configureEngine();
        try {
            this.startCoreWaker();
        } catch (Exception ec) {
            ec.printStackTrace();
        }

//		this.startSmsEngine();
    }

//	 private void startSmsEngine(){
//		 smsEngine = GeochatApi.builder().username(this.configuration.getCredential().getName()).
//		 password(this.configuration.getCredential().getPassword()).build();
//	 }
    private void configureEngine() {
        executor = Executors.newScheduledThreadPool(configuration.getCorePoolSize());

    }

    @Override
    public void stop() {
        this.stopEngine();
    }

    @Override
    public void addEngineListener(IEngineListener... engineListener) {
        Guard.argumentNotNull(engineListener, "IEngineListener");
        this.engineListener.addAll(Arrays.asList(engineListener));
    }

    @Override
    public void unRegisterListener(IWorker worker) {
        if (listenerMap.containsKey(worker.getId())) {
            listenerMap.remove(worker.getId());//create API how many adapter has been serverd and how many has cancelled
        }
    }

    @Override
    public void done(IWorker worker) {

        LOGGER.info("Work has done");
        LOGGER.info("Listener size is:" + listenerMap.size());

        Set<Map.Entry<Class<IWorker<Event>>, List<IWorkListener>>> set = listenerMap.entrySet();
        for (Map.Entry<Class<IWorker<Event>>, List<IWorkListener>> map : set) {
            LOGGER.info("Adapter name is: " + map.getKey());
            for (IWorkListener listener : map.getValue()) {
                LOGGER.info("listener name is: " + listener.getClass().getName());
            }
        }

        LOGGER.info(worker.getId() + "  work is finished .informing all listener");

        if (worker.isDaemon() && !cancelWorkList.contains(worker)) {

            //informing my listener that work is done
            try {
                workEventDispatcher.notifyWorkIsDone(worker, listenerMap);
            } catch (Exception ec) {
                ec.printStackTrace();
            }
            LOGGER.info(worker.getId() + " is a deamon and going to be active after  " + worker.wakeupDelay() + " " + worker.delayTimeUnit());
            try {
                workEventDispatcher.notifyWorkIsProcessing(worker, listenerMap);
                NotifiableFutureTask<Event> notifiableFutureTask = createFutureTask(worker);
                notifiableFutureTask.addTaskFinishListener(this);
                executor.schedule(notifiableFutureTask, worker.wakeupDelay(), worker.delayTimeUnit());
                LOGGER.info(worker.getId() + " is activated ");
            } catch (Exception ec) {
                ec.printStackTrace();
            }
        } else {
            LOGGER.info(worker.getId() + " is not a daemon or not scheduled to keep alive.so it is shutdown now ");
            engineEventDispatcher.notifyAnonymousEvent(engineListener,
                    worker.getId() + " is not a daemon or not scheduled to keep alive.so it is shutdown now ");
            workEventDispatcher.notifyWorkIsShutDown(worker, listenerMap);

            try {
                workEventDispatcher.notifyWorkIsDone(worker, listenerMap);
            } catch (Exception ec) {
                ec.printStackTrace();
            }

            if (listenerMap.containsKey(worker.getId())) {
                listenerMap.remove(worker.getId());//create API how many adapter has been serverd and how many has cancelled
            }
        }

    }

    public boolean isStop(IWorker<Event> worker) {
        return !listenerMap.containsKey(worker.getId());
    }

    public boolean isStop(Class adapterId) {
        return !listenerMap.containsKey(adapterId);
    }

    @Override
    public boolean isRunning() {
        return executor != null
                && !executor.isShutdown();
    }

    @Override
    public void cancel(IWorker<Event> worker) {
        Guard.argumentNotNull(worker, "IWorker");
        cancelWorkList.add(worker);
        engineEventDispatcher.notifyAnonymousEvent(engineListener, worker.getId() + " is scheduled to be cancelled");
    }

    @Override
    public void sendMessageToGroup(final String groupName, final String message) {
        executor.submit(new SmsWorker(new Callable<Sms>() {

            @Override
            public Sms call() throws Exception {
//				smsEngine.sendMessageToGroup(groupName, "Alert from bems engine: \n" + message).build().send();
                return new Sms(groupName, message);
            }
        }));
    }

    private class SmsWorker extends FutureTask<Sms> {

        public SmsWorker(Callable<Sms> callable) {
            super(callable);
        }

        @Override
        protected void done() {
            try {
                doneSmsSending(get());
            } catch (InterruptedException e) {
                throw new IsoapExcetion(e);
            } catch (ExecutionException e) {
                throw new IsoapExcetion(e);
            }
        }
    }

    private void doneSmsSending(Sms sms) {
        for (IEngineListener listener : engineListener) {
            listener.smsSent(sms.getTo(), sms.getBody());
        }
    }

    @Override
    public boolean isShutdown() {
        return wakerHandler.isShutdown();
    }
}
