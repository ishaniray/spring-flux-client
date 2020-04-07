package org.cerner.fluxclient;

import org.cerner.model.Employee;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

public class SpringFluxClient {

	public void getStream() {
		WebClient client = WebClient.create("http://localhost:8081");
		ParameterizedTypeReference<Employee> type = new ParameterizedTypeReference<Employee>() {
		};

		Flux<Employee> employeeStream = client.get().uri("/").retrieve().bodyToFlux(type);

		employeeStream.subscribe(employee -> System.out.println(employee.toString()),
				error -> System.out.println("An error occured: " + error),
				() -> System.out.println("This stream has now ended."));
	}

	public void doSomething() {
		System.out.println("Doing something else...");
	}
}
