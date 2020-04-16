package org.cerner.fluxclient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("org.cerner")
@SpringBootApplication
public class App {

	@Autowired
	SpringFluxClient sfc;

	@PostConstruct
	public void runApp() {
		sfc.getStream();

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(sfc::doSomething, 0, 1, TimeUnit.SECONDS);
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
