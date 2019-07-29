package br.com.fernandoarruda.http.request.retry.httpclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.http.entity.ContentType;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.com.fernandoarruda.http.request.retry.Repeater;

/**
 * Http Request Client Retry Test
 * 
 * @author Fernando Arruda (progfer@gmail.com)
 *
 */
public class HttpRequestClientRetryTest {

	@Rule
    public WireMockRule serviceMock = new WireMockRule(8089);
	
	@Test
	public void testCreateHRCR(){
		HttpRequestClientRetry hrcr = HttpRequestClientRetry.builder()
				.connectTimeout(1000)
				.socketTimeout(1000)
				.maxAttempts(2)
				.delay(2000)
				.build();
		
		assertNotNull(hrcr);
		assertEquals(1000, hrcr.getConnectTimeout());
		assertEquals(1000, hrcr.getSocketTimeout());
		assertEquals(2, hrcr.getMaxAttempts());
		assertEquals(2000, hrcr.getDelay());
	}
	
	@Test
	public void testCreateHRCRWithNegativeValues(){
		HttpRequestClientRetry hrcr = HttpRequestClientRetry.builder()
				.connectTimeout(-1000)
				.socketTimeout(-1000)
				.maxAttempts(-2)
				.delay(-2000)
				.build();
		
		assertNotNull(hrcr);
		assertEquals(HttpRequestClientRetry.DEFAULT_CONNECTION_TIMEOUT, hrcr.getConnectTimeout());
		assertEquals(HttpRequestClientRetry.DEFAULT_SOCKET_TIMEOUT, hrcr.getSocketTimeout());
		assertEquals(Repeater.MIN_ATTEMPTS, hrcr.getMaxAttempts());
		assertEquals(Repeater.MIN_DELAY, hrcr.getDelay());		
	}
	
 
	@Test(expected = HttpRequestClientException.class)
	public void testGetSocketTimeout() throws HttpRequestClientException{
		
		String jsonBody = "{ \"data\": [\"Brazil\"]}";
		serviceMock.stubFor(get(urlEqualTo("/countries"))
				.withHeader("Content-Type", equalTo(ContentType.APPLICATION_JSON.getMimeType()))
				
				.willReturn(aResponse()
						.withStatus(200)
						.withFixedDelay(2000)
						.withBody(jsonBody)));
		
		HttpRequestClientRetry hrcr = HttpRequestClientRetry.builder()
				.socketTimeout(1000)
				.maxAttempts(2)
				.build();
		
		hrcr.get("http://localhost:8089/countries", ContentType.APPLICATION_JSON.getMimeType());
		
	}
	@Test
	public void testGetSuccessRequest() throws HttpRequestClientException{
		
		String jsonBody = "{ \"data\": [\"Brazil\"]}";
		serviceMock.stubFor(get(urlEqualTo("/countries"))
				.withHeader("Content-Type", equalTo(ContentType.APPLICATION_JSON.getMimeType()))
				
				.willReturn(aResponse()
						.withStatus(200)
						.withFixedDelay(2000)
						.withBody(jsonBody)));
		
		HttpRequestClientRetry hrcr = HttpRequestClientRetry.builder()
				.connectTimeout(1000)
				.socketTimeout(5000)
				.maxAttempts(2)
				.build();
		
		String result = hrcr.get("http://localhost:8089/countries", ContentType.APPLICATION_JSON.getMimeType());
		assertEquals(jsonBody, result);		
		
	}
}