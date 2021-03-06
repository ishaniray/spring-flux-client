package org.cerner.fluxclient.subscriber;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import reactor.core.publisher.BaseSubscriber;

@Component
public class EmployeeSubscriber<Employee> extends BaseSubscriber<Employee> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeSubscriber.class);

	private int consumed = 0;
	private final int limit = 2;

	@Override
	public void hookOnSubscribe(Subscription subscription) {
		request(limit);
	}

	@Override
	public void hookOnNext(Employee employee) {
		LOGGER.info("Received: " + employee.toString());

		consumed++;

		if (consumed == limit) {
			consumed = 0;

			request(limit);
		}
	}

	@Override
	public void hookOnError(Throwable error) {
		LOGGER.error("An error occured: " + error);
	}

	@Override
	public void hookOnComplete() {
		LOGGER.info("End of stream.");
	}
}
