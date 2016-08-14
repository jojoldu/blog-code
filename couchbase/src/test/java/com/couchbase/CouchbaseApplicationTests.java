package com.couchbase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CouchbaseApplicationTests {

	private String testDirectory = "./src/test/resources/";

	@Test
	public void contextLoads() {
	}

	@Test
	public void test_UID생성() throws Exception{
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		System.out.println(CouchBaseSession.getUid(request, response));
	}

	@Test
	public void test_세션정보조회() throws Exception {


	}
}
