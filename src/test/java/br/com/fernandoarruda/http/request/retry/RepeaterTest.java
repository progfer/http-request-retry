/**
 * 
 */
package br.com.fernandoarruda.http.request.retry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import br.com.fernandoarruda.http.request.retry.httpclient.HttpRequestClientRetry;

/**
 * Repeater Test
 * 
 * @author Fernando Arruda (progfer@gmail.com)
 */
public class RepeaterTest {
	
	private static final String ERROR_MESSAGE = "Failed :(";
	private static final String OPERATION_FAILED_MESSAGE = "Failed :(";
	
	@Test
	public void testCreateRepeaterWithNegativeValues(){
		Repeater retry = Repeater.builder()
				.delay(-2000)
				.maxAttempts(-2)
				.executionErrorMessage(ERROR_MESSAGE)
				.build();
		
		assertNotNull(retry);
		assertEquals(Repeater.MIN_ATTEMPTS, retry.getMaxAttempts());
		assertEquals(Repeater.MIN_DELAY, retry.getDelay());		
	}
	
	@Test
	public void testRunnable() throws RetryExecutionException {
		Repeater retry = Repeater.builder()
						.delay(2000L)
						.maxAttempts(2)
						.executionErrorMessage(ERROR_MESSAGE)
						.build();
		retry.perform(() -> {});
	}

	@Test(expected = RetryExecutionException.class)
	public void testPerformRunnableWithRetryExecutionException() throws RetryExecutionException {
		Repeater retry = Repeater.builder()
				.delay(2000L)
				.maxAttempts(2)
				.executionErrorMessage(ERROR_MESSAGE)
				.build();
		retry.perform(() -> {
			throw new RetryOperationException(OPERATION_FAILED_MESSAGE);
		});		
	}

	@Test
	public void testPerformCallable() throws RetryExecutionException {
		Repeater retry = Repeater.builder()
				.delay(2000L)
				.maxAttempts(2)
				.executionErrorMessage(ERROR_MESSAGE)
				.build();
		
		String result = retry.perform(() -> "Hi");
		assertEquals("Hi", result);
	}

	@Test(expected = RetryExecutionException.class)
	public void testPerformCallableWithRetryExecutionException() throws RetryExecutionException {

		Repeater retry = Repeater.builder()
				.delay(2000L)
				.maxAttempts(2)
				.executionErrorMessage(ERROR_MESSAGE)
				.build();
		
		retry.perform(() -> {
			throw new RetryOperationException(OPERATION_FAILED_MESSAGE);
		});
	}

}
