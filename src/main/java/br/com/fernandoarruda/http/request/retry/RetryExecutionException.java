package br.com.fernandoarruda.http.request.retry;

/**
 * Retry Execution Exception
 * 
 * @author Fernando Arruda (progfer@gmail.com)
 */
public class RetryExecutionException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Ctor
	 * 
	 * @param message
	 *            the error message
	 */
	public RetryExecutionException(String message) {
		super(message);
	}
}
