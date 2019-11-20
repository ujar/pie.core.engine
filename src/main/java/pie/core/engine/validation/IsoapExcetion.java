package pie.core.engine.validation;
/**
 * @author Saiful Islam Raju | saiful.raju@gmail.com
 * @since version 1.0
 */
public class IsoapExcetion extends RuntimeException {

	private static final long serialVersionUID = -7137921619885853703L;

	public IsoapExcetion() {
		super();
	}

	public IsoapExcetion(String message, Throwable cause) {
		super(message, cause);
	}

	public IsoapExcetion(String message) {
		super(message);
	}

	public IsoapExcetion(Throwable cause) {
		super(cause);
	}
}
