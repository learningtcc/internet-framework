package com.clonegod.jdbc;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.clonegod.jdbc.model.User;
import com.clonegod.jdbc.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	UserService userService;
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testSaveUser() {
		int id = userService.insertUser(new User("Alice", 20, new Date()));
		System.out.println("id=" + id);
	}
	
	@Test
	public void testFindUser() {
		System.out.println(userService.findByUserName("Alice"));
	}

}
