package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.User;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

	private static final String API_BASE_URL = "http://localhost:8080/";
	private UserDAO dao;
	
	public UserController(UserDAO userDao) {
		dao = userDao;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "users", method = RequestMethod.GET)
	public List<User> list() {
		return dao.findAll();
	}
	
}
