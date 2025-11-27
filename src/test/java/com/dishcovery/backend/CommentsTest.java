package com.dishcovery.backend;


import com.dishcovery.backend.components.UserSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentsTest {


    @Autowired
    private UserSession userSession;

    @Test
    public void contextLoads() {
    }
}
