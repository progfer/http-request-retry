/**
 * 
 */
package br.com.fernandoarruda.http.request.retry.httpclient;

/**
 * Http Request Client Exception
 * 
 * @author Fernando Arruda (progfer@gmail.com)
 *
 */
public class HttpRequestClientException extends Exception{
	
	public HttpRequestClientException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
