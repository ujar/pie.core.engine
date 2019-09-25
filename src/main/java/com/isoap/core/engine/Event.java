package com.isoap.core.engine;

import org.mesh4j.feed.Feed;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class Event {

        private String name = null;
	private Feed feed;
        private IData data = null;
	
	public Event(String name){
		this.name = name;
	}
	
        
	public Event(String name,Feed feed){
		this.name = name;
		this.feed = feed;
	}

        public Event(String name,IData iData){
		this.name = name;
		this.data = iData;
	}

        public IData getData() {
            return data;
        }

        
	public Feed getFeed() {
		return feed;
	}

	public void setFeed(Feed feed) {
		this.feed = feed;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
}
