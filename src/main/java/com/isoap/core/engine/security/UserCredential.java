package com.isoap.core.engine.security;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class UserCredential implements ICredential{
	private String name;
	private String password;
	
	public UserCredential(String name,String password){
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
}
