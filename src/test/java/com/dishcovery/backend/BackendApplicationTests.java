package com.dishcovery.backend;

import com.dishcovery.backend.service.VideoServiceImplementation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private VideoServiceImplementation videoServiceImplementation;

	@Test
	void contextLoads() {
		videoServiceImplementation.transcribeVideo("51e6e049-5ac1-48cc-a9f6-1cbd1ea6cfd1");
	}

}
