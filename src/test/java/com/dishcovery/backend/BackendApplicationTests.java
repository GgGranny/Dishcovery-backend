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
//		videoServiceImplementation.transcribeVideo("51e6e049-5ac1-48cc-a9f6-1cbd1ea6cfd1");
//		videoServiceImplementation.transcribeVideoInMultipleQuality("05728de1-79de-4926-b483-b0de3cef85ad");

		videoServiceImplementation.deleteVideoById("1c85d0df-1564-4afb-8d2f-7320cf7ad0e4");
	}

}
