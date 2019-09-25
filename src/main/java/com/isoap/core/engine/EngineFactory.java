package com.isoap.core.engine;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class EngineFactory {

	private static IEngine engine = new Engine();
	

	private EngineFactory(){}
	
	public static IEngine getEngine(){
		return engine;
	}
}
