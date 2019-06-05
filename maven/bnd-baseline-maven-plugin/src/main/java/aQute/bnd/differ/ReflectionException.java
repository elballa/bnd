package aQute.bnd.differ;

/**
 * New Runtime Exception to wrap exceptions during java reflection calls.
 * 
 * @author Alessandro Ballarin
 */
@SuppressWarnings("serial")
public class ReflectionException extends RuntimeException {

	/**
	 * Create a new Runtime Exception from a raised exception.
	 * 
	 * @param cause raised exception
	 */
	public ReflectionException(Throwable cause) {
		super(cause);
	}
}
