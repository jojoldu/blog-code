package com.blogcode.security.config;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Created by jojoldu@gmail.com on 2017. 8. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(
        properties = "spring.config.location=classpath:/google.yml")
public class OAuthConfigTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void google로그인_시도하면_OAuth인증창_등장한다 () throws Exception {
        given()
                .when()
                    .redirects().follow(false) // 리다이렉트 방지
                    .get("/login")
                .then()
                    .statusCode(302)
                    .header("Location", containsString("https://accounts.google.com/o/oauth2/auth"));
    }
}
