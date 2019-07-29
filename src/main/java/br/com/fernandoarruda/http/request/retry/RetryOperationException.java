package br.com.fernandoarruda.http.request.retry;

/**
 * Retry Operation Exception
 * 
 * @author Fernando Arruda (progfer@gmail.com)
 */
public class RetryOperationException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Ctor
	 * 
	 * @param message
	 *            the error message
	 */
	public RetryOperationException(String message) {
		super(message);
	}
}