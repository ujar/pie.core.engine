package com.isoap.core.adapter;

import com.isoap.core.engine.Event;
import java.util.List;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public interface IWorkerAdapter {

    public  <T extends IWorkerAdapter> T getWorkerId();
    public List<Event> loadRealTime();
    public List<Event> loadFromPersistant();
    public List<Event> save(List<Event> events);
    public Event get(Long eventId);
	
}
