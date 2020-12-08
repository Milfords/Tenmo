package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AccountTransfer;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.User;
import com.techelevator.view.ConsoleService;


public class AccountService {
	
	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	public RestTemplate restTemplate = new RestTemplate();
	
	public AccountService(String url) {
		BASE_URL = url;
	}
	

	public BigDecimal viewCurrentBalance() throws AccountServiceException {
		BigDecimal account = null;
		try {
			account = restTemplate.exchange(BASE_URL + "balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
		} catch (RestClientResponseException ex) {
			throw new AccountServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return account;
	}
	
	public AccountTransfer[] transferList() throws AccountServiceException {
		AccountTransfer[] transferHistory = null;
		try {
			transferHistory = restTemplate.exchange(BASE_URL + "accounts/transfer/history/sent", HttpMethod.GET, makeAuthEntity(), AccountTransfer[].class).getBody();
		} catch (RestClientResponseException ex) {
			throw new AccountServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transferHistory;
	}
	public AccountTransfer[] transferList2() throws AccountServiceException {
		AccountTransfer[] transferHistory = null;
		try {
			transferHistory = restTemplate.exchange(BASE_URL + "accounts/transfer/history/received", HttpMethod.GET, makeAuthEntity(), AccountTransfer[].class).getBody();
		} catch (RestClientResponseException ex) {
			throw new AccountServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transferHistory;
	}
		
	public AccountTransfer[] transferDetails(int transferId) throws AccountServiceException {
		AccountTransfer[] transferDetails = null;
		try {
			transferDetails = restTemplate.exchange(BASE_URL + "accounts/transfer/history/details/" + transferId, HttpMethod.GET, makeAuthEntity(), AccountTransfer[].class).getBody();
			
		}catch (RestClientResponseException ex) {
			throw new AccountServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transferDetails;
	}
	
	public void transferMoney(AccountTransfer theTransfer, AuthenticatedUser currentUser) throws AccountServiceException {
		try {
			restTemplate.exchange(BASE_URL + "accounts/transfer", HttpMethod.PUT, makeAccountTransferEntity(theTransfer), AccountTransfer.class, theTransfer);
		} catch (RestClientResponseException ex) {
			 new AccountServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
	private HttpEntity<AccountTransfer> makeAccountTransferEntity(AccountTransfer transfer) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(AUTH_TOKEN);
	    HttpEntity<AccountTransfer> entity = new HttpEntity<>(transfer, headers);
	    return entity;
	  }
	
}
