package com.blogcode;

import com.blogcode.board.BoardService;
import com.blogcode.history.History;
import com.blogcode.history.HistoryRepository;
import com.blogcode.user.User;
import com.blogcode.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private BoardService boardService;

	@Autowired
	private UserService userService;

	@Autowired
	private HistoryRepository historyRepository;

	@Test
	public void findBoards() throws Exception {
		assertThat(boardService.getBoards().size()).isEqualTo(100);
	}

	@Test
	public void findUsers() throws Exception {
		assertThat(userService.getUsers().size()).isEqualTo(100);
	}

	@Test
	public void updateUsers() throws Exception {
		List<User> users = userService.getUsers();
		for(int i=0;i<5;i++){
			User user = users.get(i);
			user.setEmail("jojoldu@gmail.com");
			userService.update(user);
		}

		List<History> histories = historyRepository.findAll();
		assertThat(histories.size()).isEqualTo(5);
		assertThat(histories.get(0).getUserIdx()).isEqualTo(1L);
		assertThat(histories.get(1).getUserIdx()).isEqualTo(2L);
	}
}
