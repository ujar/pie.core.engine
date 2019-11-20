package pie.core.engine;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public interface IEngineListener {
	
	public void engineStarted();
	public void engineStopped();
	public void engineStopping();
	public void engineStopRequestd();
	public void smsSent(String to,String msg);
	public void anonymousEvent(String eventMSG);
	
}
