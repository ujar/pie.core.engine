package pie.core.engine;

import pie.core.engine.security.ICredential;
import org.mesh4j.feed.security.IIdentityProvider;

/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public interface IConfiguration {

	public int getCorePoolSize();
	public int getMaxPoolSize();
	public IIdentityProvider getIdentityProvider();
	public ICredential getCredential();
	public boolean isServer();
	
}
