package com.isoap.core.engine.security;

import com.isoap.core.engine.IConfiguration;
import org.mesh4j.feed.security.IIdentityProvider;
import org.mesh4j.feed.security.LoggedInIdentityProvider;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class SimpleConfiguration implements IConfiguration{

	private int corePoolSize ; 
	private int maxPoolSize;
	private ICredential broadCastingCredential = null;
	private boolean isServer = false;
	
	
	public SimpleConfiguration(int corePoolSize , int maxPoolSize,ICredential broadCastingCredential,
			boolean isServer){
		this.corePoolSize = corePoolSize;
		this.maxPoolSize = maxPoolSize;
		this.broadCastingCredential = broadCastingCredential;
		this.isServer = isServer;
	}
	
	@Override
	public int getCorePoolSize() {
		return this.corePoolSize;
	}

	@Override
	public IIdentityProvider getIdentityProvider() {
		return new LoggedInIdentityProvider();
	}

	@Override
	public int getMaxPoolSize() {
		return this.maxPoolSize;
	}

	@Override
	public ICredential getCredential() {
		return this.broadCastingCredential;
	}

	@Override
	public boolean isServer() {
		return isServer;
	}

}
