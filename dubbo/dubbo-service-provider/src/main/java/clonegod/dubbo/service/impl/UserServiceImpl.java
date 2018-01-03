package clonegod.dubbo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clonegod.dubbo.api.IUserService;
import clonegod.dubbo.api.User;

public class UserServiceImpl implements IUserService {
	
	static Map<Long, User> USERS = new HashMap<>();
	
	static {
		try {
			User alice = new User(1L, "alice", 21, new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-01"));
			User bob = new User(2L, "bob",   22, new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-02"));
			User cindy = new User(3L, "cindy", 23, new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-03"));
			USERS.put(alice.getId(), alice);
			USERS.put(bob.getId(), bob);
			USERS.put(cindy.getId(), cindy);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String sayHello(String name) {
		return "Hello: " + name;
	}

	@Override
	public User getById(long id) {
		return USERS.get(id);
	}

	@Override
	public List<User> getAllUsers() {
		return new ArrayList<>(USERS.values());
	}

}
