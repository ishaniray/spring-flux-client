package org.cerner.fluxclient.subscriber;

import org.cerner.fluxclient.service.DynamicBackpressureService;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.publisher.BaseSubscriber;

@Component
public class EmployeeSubscriberWithDynamicBackpressure<Employee> extends BaseSubscriber<Employee> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeSubscriberWithDynamicBackpressure.class);

	@Autowired
	private DynamicBackpressureService dbs;

	private int consumed = 0;
	private int limit = 0;

	@Override
	public void hookOnSubscribe(Subscription subscription) {
		limit = dbs.getCurrentBackPressure();
		LOGGER.info("hookOnSubscribe(): Requesting for " + limit + " items.");
		request(limit);
	}

	@Override
	public void hookOnNext(Employee employee) {
		LOGGER.info("Received: " + employee.toString());

		consumed++;

		if (consumed == limit) {
			consumed = 0;
			limit = dbs.getCurrentBackPressure();
			LOGGER.info("hookOnNext(): Requesting for " + limit + " items.");
			request(limit);
		}
	}

	@Override
	public void hookOnError(Throwable error) {
		LOGGER.error("An error occured: " + error.getMessage());
	}

	@Override
	public void hookOnComplete() {
		LOGGER.info("End of stream.");
	}
}
