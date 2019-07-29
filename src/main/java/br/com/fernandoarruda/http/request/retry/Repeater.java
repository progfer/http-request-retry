package br.com.fernandoarruda.http.request.retry;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operations Repeater
 * 
 * @author Fernando Arruda (progfer@gmail.com)
 */
public final class Repeater {
	private static final Logger log = LoggerFactory.getLogger(Repeater.class);

	public static int MIN_ATTEMPTS = 1;
	public static int MIN_DELAY = 0;
	
	private int maxAttempts;
	private long delay;
	private String executionErrorMessage;

	private Repeater(int maxAttempts, long delay, String executionErrorMessage) {
		this.maxAttempts = (maxAttempts < MIN_ATTEMPTS)? MIN_ATTEMPTS : maxAttempts;
		this.delay = (delay < MIN_DELAY)? MIN_DELAY : delay;
		
		this.executionErrorMessage = executionErrorMessage;
	}
	
	/**
	 * @return the maxAttempts
	 */
	public int getMaxAttempts() {
		return maxAttempts;
	}
	
	/**
	 * @return the delay
	 */
	public long getDelay() {
		return delay;
	}

	public <V> V perform(Callable<V> callable) throws RetryExecutionException {
		return retryLogics(callable);
	}

	public void perform(RetryOperation command) throws RetryExecutionException {
		retryLogics(() -> {
			command.run();
			return null;
		});
	}

	private <T> T retryLogics(Callable<T> callable) throws RetryExecutionException {
		int counter = 0;
		String lastMessage = null;
		do {
			try {
				
				return callable.call();

			} catch (Exception ex) {

				lastMessage = ex.getMessage();
				
				counter++;
				
				if (!(ex instanceof RetryOperationException)) {
					log.error("Retry Operation {} / {}, Attempt to perform untreated operation", counter, maxAttempts, ex);
					break;
				}
				
				log.error("Retry Operation {} / {}", counter, maxAttempts, ex);
				
				try {
					TimeUnit.MILLISECONDS.sleep(delay);
				} catch (InterruptedException e1) {
					// ignore
				}
			}
		} while (counter < maxAttempts);

		
		throw new RetryExecutionException(String.format("%s - %s", executionErrorMessage, lastMessage));
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
		private String executionErrorMessage;
		
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
		 * Set Execution error message
		 * @param message
		 * @return {@link Builder} instance
		 */
		public Builder executionErrorMessage(String message) {
			this.executionErrorMessage = message;
			return this;
		}

		/**
		 * Build Repeater
		 * 
		 * @return {@link Repeater} instance
		 */
		public Repeater build() {
			return new Repeater(maxAttempts, delay, executionErrorMessage);
		}
	}
}
