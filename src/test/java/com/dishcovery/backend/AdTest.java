package com.dishcovery.backend;

import com.dishcovery.backend.service.AdServiceImple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class AdTest {

    @Autowired
    private AdServiceImple adServiceImple;

    @Test
    public void testAdUpload() {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
//        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(WebApplicationContext).build();

        adServiceImple.uploadAd(multipartFile);
    }

}
