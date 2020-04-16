package org.cerner.fluxclient.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicBackpressureService {

	public static final int[] BACKPRESSURE_VALUES = { 2, 4, 8 };

	@Autowired
	private Random random;

	public int getCurrentBackPressure() {
		return BACKPRESSURE_VALUES[random.nextInt(3)];
	}
}
