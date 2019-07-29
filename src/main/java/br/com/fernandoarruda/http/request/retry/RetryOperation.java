package br.com.fernandoarruda.http.request.retry;

/**
 * Retry Operation
 * 
 * @author Fernando Arruda (progfer@gmail.com)
 */
@FunctionalInterface
public interface RetryOperation {
	
	void run() throws RetryExecutionException;
}