package com.isoap.core.engine;

import java.util.Date;
import java.util.List;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public interface IDataLoaderAdapter {

	public String getId();
	public String getDescription();
	public String getUrl();
	public String getAbsoluteUrl();
	
	
	public List<Event> load(Date from , Date to);
	public List<Event> loadAll();
	
	public void start();
	public void stop();
	 
	
	
}
