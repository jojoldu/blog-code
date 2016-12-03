package com.blogcode;

import com.blogcode.board.BoardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	//@Qualifier("boardServicePerformance")
	private BoardService boardService;

	@Test
	public void findBoards() throws Exception {
		assertThat(boardService.getBoards().size()).isEqualTo(100);
	}

}
