package org.cerner.fluxclient.subscriber;

import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;

public class EmployeeSubscriber<Employee> extends BaseSubscriber<Employee> {

	int consumed = 0;
	final int limit = 2;

	@Override
	public void hookOnSubscribe(Subscription subscription) {
		request(limit);
	}

	@Override
	public void hookOnNext(Employee employee) {
		System.out.println(employee.toString());

		consumed++;

		if (consumed == limit) {
			consumed = 0;

			request(limit);
		}
	}

	@Override
	public void hookOnError(Throwable error) {
		System.out.println("An error occured: " + error);
	}

	@Override
	public void hookOnComplete() {
		System.out.println("This stream has now ended.");
	}
}
