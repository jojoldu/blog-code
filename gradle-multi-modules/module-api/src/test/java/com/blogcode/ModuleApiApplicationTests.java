package com.blogcode;

import com.blogcode.domain.Member;
import com.blogcode.service.MemberServiceCustom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ModuleApiApplicationTests {

	@Autowired
	private MemberServiceCustom memberServiceCustom;

	@Test
	public void save() {
		Member member = new Member("jojoldu", "jojoldu@gmail.com");
		Long id = memberServiceCustom.signup(member);
		assertThat(id, is(1L));
	}
}
