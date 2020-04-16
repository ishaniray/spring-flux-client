package org.cerner.fluxclient;

import org.cerner.fluxclient.subscriber.EmployeeSubscriber;
import org.cerner.fluxclient.subscriber.EmployeeSubscriberWithDynamicBackpressure;
import org.cerner.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Component
public class SpringFluxClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringFluxClient.class);

	@Autowired
	private EmployeeSubscriber<Employee> employeeSubscriber;
	@Autowired
	private EmployeeSubscriberWithDynamicBackpressure<Employee> employeeSubscriberWithDynamicBackpressure;

	public void getStream() {
		WebClient client = WebClient.create("http://localhost:8081");
		ParameterizedTypeReference<Employee> type = new ParameterizedTypeReference<Employee>() {
		};

		Flux<Employee> employeeStream = client.get().uri("/").retrieve().bodyToFlux(type);

		// Approach 1: Lambda Consumers - onNext, onError, onComplete
		employeeStream.subscribe(employee -> LOGGER.info("Received: " + employee.toString()),
				error -> LOGGER.error("An error occured: " + error), () -> LOGGER.info("End of stream."));

		// Approach 2: Approach 1 with Backpressure and Backpressure Buffer
		employeeStream.onBackpressureBuffer().subscribe(employee -> LOGGER.info("Received: " + employee.toString()),
				error -> LOGGER.error("An error occured: " + error), () -> LOGGER.info("End of stream."),
				subscription -> subscription.request(3));

		// Approach 3: Writing a subscriber class (extend BaseSubscriber)
		employeeStream.subscribe(employeeSubscriber);

		// Approach 4: Approach 3 with Dynamic Backpressure
		employeeStream.subscribe(employeeSubscriberWithDynamicBackpressure);

	}

	public void doSomething() {
		LOGGER.debug("Doing something else...");
	}
}
