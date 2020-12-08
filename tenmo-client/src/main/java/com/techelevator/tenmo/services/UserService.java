package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.User;
import com.techelevator.view.ConsoleService;

public class UserService {

	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	// made an empty constructor to implement ConsoleService
	private final ConsoleService console = new ConsoleService();
	
	
	public UserService(String url) {
        BASE_URL = url;
    }
	
	public User[] list() throws UserServiceException {
		User[] userInfo = null;
		try {
			userInfo = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeUserEntity(), User[].class).getBody();
		} catch (RestClientResponseException e) {
			throw new UserServiceException(e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
		}
		return userInfo;
	}

	private HttpEntity makeUserEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
	}
}
