package com.isoap.core.engine.security;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class Sms {
	
	private String to;
	private String body;
	
	public Sms(String to, String body) {
		super();
		this.to = to;
		this.body = body;
	}

	public String getTo() {
		return to;
	}

	public String getBody() {
		return body;
	}
}
