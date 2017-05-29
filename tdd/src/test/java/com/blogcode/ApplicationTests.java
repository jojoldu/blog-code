package com.blogcode;

import com.blogcode.domain.Member;
import com.blogcode.exception.NotBeNegativeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	/*
		실패하는 테스트 코드 작성 -> 프로덕트 코드 작성 -> 테스트 코드가 통과되도록 코드 수정
		-> 테스트 코드 통과

	 */
	@Test
	public void 년월을_입력하면_나이가_나온다() {
		Member member = new Member(1987,9,12);
		int age = member.calculateAge();

		assertThat(age, is(31));

		Member member1 = new Member(1999, 9, 12);
		int age2 = member1.calculateAge();

		assertThat(age2, is(19));
	}

	@Test(expected = NotBeNegativeException.class)
	public void 마이너스가_입력되면_예외가발생한다(){
		Member member = new Member(-1,12,9);
		int age = member.calculateAge();

	}

}
