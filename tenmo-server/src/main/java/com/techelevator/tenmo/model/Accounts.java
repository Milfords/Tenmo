package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Accounts {

	private int accountId;
	private int userId;
	private BigDecimal balance;
	
	public Accounts(int accountId, int userId, BigDecimal balance) {
		this.accountId = accountId;
		this.balance = balance;
		this.userId = userId;
	}
	
	public Accounts() {
		
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	
}
