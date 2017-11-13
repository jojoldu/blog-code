package com.jojoldu.webenv;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static com.jojoldu.webenv.Application.CONFIG_LOCATIONS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

/**
 * Created by jojoldu@gmail.com on 2017. 11. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
//@TestPropertySource(properties = CONFIG_LOCATIONS)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public abstract class WebIntegrationTest {

    @Autowired
    protected WebApplicationContext wac;
}
