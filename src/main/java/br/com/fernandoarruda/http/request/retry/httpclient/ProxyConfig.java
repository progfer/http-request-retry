/**
 * 
 */
package br.com.fernandoarruda.http.request.retry.httpclient;

/**
 * Proxy Configuration
 * 
 * @author Fernando Arruda (progfer@gmail.com)
 *
 */
public final class ProxyConfig {

	private String host;
	private int port;
	private String username;
	private String password;
	private boolean requiredAuth;

	private ProxyConfig() {}
	
	private ProxyConfig(String host, int port, String username, String password, boolean requiredAuth) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.requiredAuth = requiredAuth;
	}

	public static ProxyConfig noProxy() {
		return new ProxyConfig();
	}

	public static ProxyConfig proxyWithAuthentication(String host, int port, String username, String password) {
		return new ProxyConfig(host, port, username, password, true);
	}

	public static ProxyConfig proxyWithoutAuthentication(String host, int port) {
		return new ProxyConfig(host, port, null, null, false);
	}

	/**
	 * @return the hasAuthentication
	 */
	public boolean hasAuthentication() {
		return requiredAuth;
	}
	
	public boolean isNoProxy(){
		return host == null;
	}
 

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	
}
