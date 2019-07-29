# Http Request Retry
Example http request with retry and interval control

## Version
1.0.0

## Tech Stack
* Java 8
* Maven >=3
* HTTPCOMPONENTS - Fluent API - 4.5.8
* Logback Classic - 1.2.3
* Junit - 4.11
* Wiremock - 2.5.1

## Roadmap
 - Implement POST method

## Create Repeater

```java
        // Runnable mode without return
		Repeater retry = Repeater.builder()
				.delay(2000L)
				.maxAttempts(2)
				.executionErrorMessage(ERROR_MESSAGE)
				.build();
		retry.perform(() -> {
			throw new RetryOperationException(OPERATION_FAILED_MESSAGE);
		});	

        // Callable mode with return
		Repeater retry = Repeater.builder()
				.delay(2000L)
				.maxAttempts(2)
				.executionErrorMessage(ERROR_MESSAGE)
				.build();
		
		String result = retry.perform(() -> "Hi");
		assertEquals("Hi", result);
```

## Create Http Request Retry

```java
		HttpRequestClientRetry hrcr = HttpRequestClientRetry.builder()
				.connectTimeout(5000)
				.socketTimeout(5000)
				.maxAttempts(2)
                .delay(2000) // 2 seconds
				.build();
		
		String result = hrcr.get("http://localhost:8089/countries", ContentType.APPLICATION_JSON.getMimeType());
```

## Running the project

### Run all the unit test classes.

	$ mvn test

