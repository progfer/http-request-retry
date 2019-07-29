package br.com.fernandoarruda.http.request.retry.httpclient;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import br.com.fernandoarruda.http.request.retry.Repeater;
import br.com.fernandoarruda.http.request.retry.RetryExecutionException;
import br.com.fernandoarruda.http.request.retry.RetryOperationException;

/**
 * Http Request Client Retry
 * 
 * @author Fernando Arruda (progfer@gmail.com)
 */
public final class HttpRequestClientRetry {
	public static String HEADER_CONTENT_TYPE = "Content-Type";
	public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
	public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
	
	private final Repeater repeater;
	private int connectTimeout;
	private int socketTimeout; 
	private HttpHost proxy;
	
	private HttpRequestClientRetry(int maxAttempts, long delay, 
			int connectTimeout,
			int socketTimeout,
			ProxyConfig proxyConfig){
		this.connectTimeout = (connectTimeout < 0)? DEFAULT_CONNECTION_TIMEOUT : connectTimeout;
		this.socketTimeout = (socketTimeout < 0)? DEFAULT_SOCKET_TIMEOUT : socketTimeout;
		
		repeater = Repeater.builder()
					.maxAttempts(maxAttempts)
					.delay(delay)
					.executionErrorMessage("Http Request Error")
					.build();
		proxyConfig = (proxyConfig == null)? ProxyConfig.noProxy() : proxyConfig;
		
		if(!proxyConfig.isNoProxy()){
			
			//TODO: if(proxyConfig.hasAuthentication()){
			proxy = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort());
		}
	}
	
	/**
	 * @return the connectTimeout
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}
	
	/**
	 * @return the socketTimeout
	 */
	public int getSocketTimeout() {
		return socketTimeout;
	}
	
	/**
	 * @return the maxAttempts
	 */
	public int getMaxAttempts() {
		return repeater.getMaxAttempts();
	}
	
	/**
	 * @return the delay
	 */
	public long getDelay() {
		return repeater.getDelay();
	}
	
	public String get(String uri, String contentType) throws HttpRequestClientException{
			
		if(uri == null || "".equals(uri)){
			throw new IllegalArgumentException("Uri was not informed");
		}
		
		try {
			return repeater.perform(()->{
				try {
					return Request.Get(uri)
						. connectTimeout(connectTimeout)
						.socketTimeout(socketTimeout)
						.viaProxy(proxy)
						.addHeader(HEADER_CONTENT_TYPE, contentType)
						.execute().returnContent().asString();
				} catch (IOException e) {
					throw new RetryOperationException(String.format("URI: %s, %s", uri, e.getMessage()));
				}
				
			});
		} catch (RetryExecutionException ex) {
			throw new HttpRequestClientException(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Create Builder
	 * 
	 * @return {@link Builder}
	 */
	public static Builder builder(){
		return new Builder();
	}


	public static class Builder {

		private int maxAttempts;
		private long delay; 
		private int connectTimeout;
		private int socketTimeout;
		private ProxyConfig proxyConfig;

		/**
		 * Set Max Attempts
		 * 
		 * @param maxAttempts
		 * @return {@link Builder} instance
		 */
		public Builder maxAttempts(int maxAttempts) {
			this.maxAttempts = maxAttempts;
			return this;
		}
		/**
		 * Set Delay in Milliseconds
		 * 
		 * @param delay
		 * @return {@link Builder} instance
		 */
		public Builder delay(long delay) {
			this.delay = delay;
			return this;
		}
		
		/**
		 * Set Connection Timeout in Milliseconds
		 * 
		 * @param connectTimeout
		 * @return {@link Builder} instance
		 */
		public Builder connectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
			return this;
		}
		/**
		 * Set Socket Timeout in Milliseconds
		 * 
		 * @param socketTimeout
		 * @return {@link Builder} instance
		 */
		public Builder socketTimeout(int socketTimeout) {
			this.socketTimeout = socketTimeout;
			return this;
		}
		/**
		 * Set Proxy Configuration
		 * 
		 * @param proxyConfig
		 * @return {@link Builder} instance
		 */
		public Builder proxyConfig(ProxyConfig proxyConfig) {
			this.proxyConfig = proxyConfig;
			return this;
		}
		
		/**
		 * Build Repeater
		 * 
		 * @return {@link Repeater} instance
		 */
		public HttpRequestClientRetry build() {
			return new HttpRequestClientRetry(maxAttempts, delay, connectTimeout, socketTimeout, proxyConfig);
		}
	}
	 
}